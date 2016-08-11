package net.segoia.event.eventbus.peers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.EventClassMatchCondition;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.SimpleEventDispatcher;
import net.segoia.event.eventbus.constants.EventParams;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.peers.events.EventNodeInfo;
import net.segoia.event.eventbus.peers.events.NodeTerminateEvent;
import net.segoia.event.eventbus.peers.events.PeerBindConfirmation;
import net.segoia.event.eventbus.peers.events.PeerBindConfirmedEvent;
import net.segoia.event.eventbus.peers.events.PeerBindRequestEvent;
import net.segoia.event.eventbus.peers.events.PeerBindResponse;
import net.segoia.event.eventbus.peers.events.PeerBindResponseEvent;
import net.segoia.event.eventbus.peers.events.PeerLeavingEvent;
import net.segoia.event.eventbus.peers.events.PeerRegisterRequestEvent;
import net.segoia.event.eventbus.peers.events.PeerRegisterRequestEvent.Data;
import net.segoia.event.eventbus.peers.events.PeerRegisteredEvent;
import net.segoia.event.eventbus.peers.events.PeerRequestUnregisterEvent;
import net.segoia.event.eventbus.peers.events.PeerUnregisteredEvent;
import net.segoia.event.eventbus.peers.routing.RoutingTable;
import net.segoia.event.eventbus.util.EBus;
import net.segoia.util.data.SetMap;

/**
 * This encapsulates the connection with another event bus
 * 
 * @author adi
 *
 */
public abstract class EventNode {
    private String id;
    protected EventBusNodeConfig config;

    /**
     * keep the relays associated with peers
     */
    private Map<String, EventRelay> directPeers = new HashMap<>();

    private Map<String, EventRelay> remotePeers = new HashMap<>();

    private RoutingTable routingTable = new RoutingTable();
    
    private EventNodeStats stats = new EventNodeStats();

    /**
     * if true, it will call {@link #init()} from the constructor, otherwise somebody else will have to call
     * {@link #lazyInit()}
     */
    private boolean autoinit = true;
    private boolean initialized;

    /**
     * This is used to delegate events to internal handlers
     */
    private FilteringEventBus internalBus;

    public EventNode(String id, boolean autoinit, EventBusNodeConfig config) {
	this.id = id;
	this.config = config;
	this.autoinit = autoinit;

	if (autoinit) {
	    init();
	}
    }

    public EventNode(String id, boolean autoinit) {
	this(id, autoinit, new EventBusNodeConfig());
    }

    public EventNode() {
	this(true);
    }

    public EventNode(boolean autoinit) {
	/* generate an id for ourselves */
	this(generatePeerId(), autoinit);
    }

    public EventNode(String id) {
	this(id, true);
    }

    public EventNode(EventBusNodeConfig config) {
	this(generatePeerId(), true, config);
    }

    private void init() {
	registerHandlers();
	nodeConfig();
	setRequestedEventsCondition();
	nodeInit();
	initialized = true;
    }

    /**
     * Call this to lazy initialize this node
     */
    public void lazyInit() {
	if (!autoinit && !initialized) {
	    init();
	}
    }

    private void initInternalBus() {
	if (internalBus == null) {
	    /* use this if you want a private thread for this node */
	    // internalBus = EBus.buildSingleThreadedAsyncFilteringEventBus(100, new EventNodeDispatcher());

	    /* by default this node will function on the main loop bus */
	    internalBus = EBus.buildFilteringEventBusOnMainLoop(new EventNodeDispatcher());
	}
    }

    /**
     * A subclass may implement this to initialize itself
     */
    protected abstract void nodeInit();

    /**
     * A subclass may implement this to set up extra stuff
     */
    protected abstract void nodeConfig();

    /**
     * Override this to register handlers, but don't forget to call super or you'll lose basic functionality
     */
    protected void registerHandlers() {

	addEventHandler(PeerBindRequestEvent.class, (c) -> {

	    PeeringRequest req = c.getEvent().getData();
	    EventRelay localRelay = buildRelay(req);

	    PeerBindResponse resp = new PeerBindResponse(localRelay, getPeeringRequest());
	    PeerBindResponseEvent respEvent = new PeerBindResponseEvent(resp);
	    respEvent.to(req.getRequestingNode().getId());

	    req.getRequestingNode().postInternally(respEvent);
	});

	addEventHandler(PeerBindResponseEvent.class, (c) -> {
	    PeerBindResponse resp = c.getEvent().getData();

	    EventRelay remoteRelay = resp.getRelay();
	    EventRelay localRelay = buildRelay(resp.getBindRequest());

	    directPeers.put(remoteRelay.getParentNodeId(), localRelay);
	    remoteRelay.bind(localRelay);

	});

	addEventHandler(PeerBindConfirmedEvent.class, (c) -> {
	    PeerBindConfirmation data = c.getEvent().getData();
	    EventRelay localRelay = data.getRelay();
	    String peerId = localRelay.getRemoteNodeId();
	    directPeers.put(peerId, localRelay);
	    localRelay.bindAccepted(this);
	    onNewPeer(peerId);
	});

	addEventHandler("EBUS:PEER:NEW", (c) -> {
	    Event event = c.getEvent();
	    String peerId = (String) event.getParam(EventParams.peerId);
	    String lastRelay = event.getLastRelay();
	    if (lastRelay != null) {
		routingTable.addRoute(peerId, lastRelay);
	    }
	});

	addEventHandler("EBUS:PEER:REMOVED", (c) -> {
	    Event event = c.getEvent();
	    String removedPeerId = (String) event.getParam(EventParams.peerId);
	    String lastRelay = event.getLastRelay();
	    if (lastRelay != null) {
		routingTable.removeAllFor(removedPeerId);
	    }
	});

	addEventHandler(NodeTerminateEvent.class, (c) -> {
	    EventNodeInfo nodeInfo = c.getEvent().getData();
	    /* accept this command only from us */
	    if (nodeInfo.getNode() != this) {
		return;
	    }

	    onTerminate();
	    /* call terminate on all peer relays */
	    for (EventRelay relay : directPeers.values()) {
		relay.terminate();
	    }
	    if (internalBus != null) {
		internalBus.stop();
	    }
	    initialized = false;
	    /* then clean up */
	    cleanUp();

	});

	addEventHandler(PeerLeavingEvent.class, (c) -> {
	    String peerId = c.getEvent().getData().getPeerId();
	    removePeer(peerId);
	    onPeerRemoved(peerId);
	});

	addEventHandler("PEER:REQUEST:REGISTER", (c) -> {
	    Event event = c.getEvent();
	    PeerRegisterRequestEvent prre = (PeerRegisterRequestEvent) event;
	    registerRemotePeer(prre);
	    updateRoute(event);
	});

	addEventHandler("PEER:REQUEST:UNREGISTER", (c) -> {
	    Event event = c.getEvent();
	    unregisterRemotePeer((PeerRequestUnregisterEvent) event);
	});

	/* if autorelay enabled, then forward the event to the peers that weren't already targeted by the event */
	addEventHandler((c) -> {
	    Event event = c.getEvent();
	    if (!config.isAutoRelayEnabled() || event.wasRelayedBy(getId()) || !event.getForwardTo().isEmpty() || event.to() != null ) {
		return;
	    }

	    getPeers().forEach((peerId, relay) -> {
//		if (!event.getForwardTo().contains(peerId) && !peerId.equals(event.to())) {
		    relay.onLocalEvent(c);
//		}
	    });
	});

    };

    protected abstract void setRequestedEventsCondition();

    public void terminate() {
	forwardTo(new NodeTerminateEvent(this), getId());
    }

    public abstract void cleanUp();

    protected abstract void onTerminate();

    private void updateRoute(Event event) {
	String from = event.from();
	String via = event.getLastRelay();

	if (from != null && !from.equals(via)) {
	    routingTable.addRoute(from, via);
	}
    }

    public void registerPeer(EventNode peerNode) {
	registerPeer(new PeeringRequest(peerNode));
    }

    public void registerPeer(EventNode peerNode, Condition condition) {
	registerPeer(new PeeringRequest(peerNode, condition));
    }

    public synchronized void registerPeer(PeeringRequest request) {
	// getRelayForPeer(request, true);
	forwardTo(new PeerBindRequestEvent(request), getId());

    }

    public void unregisterPeer(EventNode peerNode) {
	String peerId = peerNode.getId();
	removePeer(peerId);
	peerNode.removePeer(getId());

	// TODO: handle any errors that appear during removal
	onPeerRemoved(peerId);
    }

    /**
     * Called when a direct peer has called {@link #terminate()}
     * 
     * @param peerId
     */
    public void onPeerLeaving(String peerId) {
	/* process this through the internal bus */
	forwardTo(new PeerLeavingEvent(peerId), getId());
    }

    /**
     * Called after a peer has been unregistered </br>
     * By default sends an EBUS:PEER:REMOVED message </br>
     * Override this if you want specific behavior
     * 
     * @param peerNode
     */
    protected void onPeerRemoved(String peerId) {
	Event nne = Events.builder().ebus().peer().peerRemoved().topic(getId()).build();
	nne.addParam(EventParams.peerId, peerId);
	forwardToAll(nne);
    }

    protected void removePeer(String peerId) {
	EventRelay localRelay = getDirectPeerRelay(peerId);
	if (localRelay != null) {
	    /* call cleanUp not terminate */
	    localRelay.cleanUp();
	}
	directPeers.remove(peerId);
	routingTable.removeAllFor(peerId);
    }

    protected boolean setRelayForwardingCondition(EventRelay relay, Condition condition) {
	// TODO: check if the condition is allowed by this node
	relay.setForwardingCondition(condition);
	return true;
    }

    protected EventRelay getRelayForPeer(PeeringRequest req, boolean create) {
	EventNode peerNode = req.getRequestingNode();
	EventRelay localRelay = getDirectPeerRelay(peerNode.getId());

	if (localRelay == null && create) {
	    localRelay = addPeer(req);
	    EventRelay remoteRelay = peerNode.addPeer(getPeeringRequest());
	    localRelay.bind(remoteRelay);
	    /* start relays */
	    remoteRelay.start();
	    localRelay.start();

	    onNewPeer(peerNode.getId());
	} else if (localRelay != null) {
	    /* if the relay exists, check it the condition has changed */
	    setRelayForwardingCondition(localRelay, req.getEventsCondition());
	}

	return localRelay;
    }

    /**
     * Called after a new peer has been registered </br>
     * By default sends an EBUS:PEER:NEW event </br>
     * Override this if you want specific behavior
     * 
     * @param peerNode
     */
    protected void onNewPeer(String peerId) {
	Event nne = Events.builder().ebus().peer().newPeer().topic(getId()).build();
	nne.addParam("peerId", peerId);
	forwardToAll(nne);
    }

    protected PeeringRequest getPeeringRequest() {
	return new PeeringRequest(this, config.getDefaultRequestedEvents());
    }

    protected EventRelay addPeer(PeeringRequest req) {
	EventNode peerNode = req.getRequestingNode();
	String peerId = generatePeerId();
	EventRelay localRelay = buildLocalRelay(peerId);
	directPeers.put(peerNode.getId(), localRelay);
	localRelay.init();

	setRelayForwardingCondition(localRelay, req.getEventsCondition());

	return localRelay;
    }

    protected EventRelay buildRelay(PeeringRequest req) {
	String relayId = generatePeerId();
	EventRelay localRelay = buildLocalRelay(relayId);
	localRelay.init();

	setRelayForwardingCondition(localRelay, req.getEventsCondition());

	return localRelay;
    }

    // /**
    // * Called for all events, remote or internally generated
    // *
    // * @param event
    // * @return
    // */
    // protected abstract EventTracker handleEvent(Event event);

    protected void removeEventHandler(CustomEventHandler<?> handler) {
	internalBus.removeListener(handler);
    }

    protected void addEventHandler(Class<?> eventClass, CustomEventHandler<?> handler) {
	addBusHandler(new EventClassMatchCondition(eventClass), handler);
    }

    protected void addEventHandler(String eventType, CustomEventHandler<?> handler) {
	addBusHandler(new StrictEventMatchCondition(eventType), handler);
    }

    protected <E extends Event> void addEventHandler(Class<E> eventClass, EventHandler<E> handler) {
	addEventHandler(eventClass, new CustomEventHandler<>(handler));
    }

    protected <E extends Event> void addEventHandler(String eventType, EventHandler<E> handler) {
	addEventHandler(eventType, new CustomEventHandler<>(handler));
    }

    private void addBusHandler(Condition cond, CustomEventHandler<?> handler) {
	initInternalBus();
	internalBus.registerListener(cond, handler);
    }

    protected <E extends Event> void addEventHandler(EventHandler<E> handler) {
	addBusHandler(new CustomEventHandler<>(handler));
    }

    private void addBusHandler(CustomEventHandler<?> handler) {
	initInternalBus();
	internalBus.registerListener(handler);
    }

    /**
     * Handle a remote event
     * 
     * @param pc
     * @return - true if we decide that this event is meant for us, false otherwise
     */
    protected boolean handleRemoteEvent(EventContext pc) {
	Event event = pc.getEvent();

	boolean forUs = false;
	/* if this event is sent to one of our peers then forward it to them */
	String destination = event.to();
	if (destination != null) {
	    forUs = destination.equals(getId());
	    if (!forUs) {
		forwardTo(event, destination);
	    }
	} else {
	    /* see if this has forwardTo peers */
	    Set<String> forwardTo = event.getForwardTo();
	    if (!forwardTo.isEmpty()) {
		/* check if we're targeted by this event */
		forUs = forwardTo.remove(getId());
		
		/* forward to the others if any */
		if(forwardTo.size() > 0 ) {
		    forwardTo(event, forwardTo);
		}

	    } else {
		// forwardToAll(event);
		/* if this is a public event we can process it too */
		forUs = true;
	    }

	}

	return forUs;
    }

    protected void postInternally(Event event) {

	if (internalBus != null && initialized) {
	    internalBus.postEvent(event);
	}
    }

    /**
     * Called for all received remote events
     * 
     * @param pc
     */
    public void onRemoteEvent(PeerEventContext pc) {
	postInternally(pc.getEvent());
    }

    protected void registerRemotePeer(PeerRegisterRequestEvent event) {
	Data ed = event.getData();
	String peerId = ed.getPeerId();
	// if (getId().equals(peerId)) {
	// throw new RuntimeException("Can't register on ourselves");
	// }
	EventRelay eventRelay = null;
	eventRelay = remotePeers.get(peerId);
	if (eventRelay == null) {
	    eventRelay = addRemotePeer(peerId);
	}

	eventRelay.setForwardingCondition(ed.getEventsCondition());

	sendPeerRegisteredEvent(peerId);
    }

    private void sendPeerRegisteredEvent(String peerId) {
	PeerRegisteredEvent e = new PeerRegisteredEvent(peerId);
	forwardToAll(e);
    }

    protected EventRelay addRemotePeer(String peerId) {
	EventRelay relay = buildRemotePeerRelay(generatePeerId());
	remotePeers.put(peerId, relay);
	relay.init();
	return relay;
    }

    protected EventRelay buildRemotePeerRelay(String relayId) {
	return new DefaultEventRelay(relayId, this);
    }

    protected void unregisterRemotePeer(PeerRequestUnregisterEvent event) {
	removeRemotePeer(event.getData().getPeerId());
    }

    protected void removeRemotePeer(String peerId) {
	EventRelay relay = remotePeers.remove(peerId);
	if (relay != null) {
	    relay.terminate();
	    sendPeerUnregisteredEvent(peerId);
	}
    }

    private void sendPeerUnregisteredEvent(String peerId) {
	PeerUnregisteredEvent e = new PeerUnregisteredEvent(peerId);
	forwardToAll(e);
    }

    protected void forwardToAll(Event event) {
	EventContext ec = new EventContext(event, null);
	for (EventRelay r : getPeers().values()) {
	    r.onLocalEvent(ec);
	}
    }

    protected void forwardTo(Event event, String to) {
	event.to(to);
	if (getId().equals(to)) {
	    postInternally(event);
	    return;
	}

	EventContext ec = new EventContext(event, null);
	/* first check if the destination is one of the peers */
	EventRelay peerRelay = directPeers.get(to);
	/* if it is, forward the event */
	if (peerRelay != null) {
	    peerRelay.onLocalEvent(ec);
	}
	/* otherwise, set destination and forward it to the peers */
	else if (!event.wasRelayedBy(getId())) {
	    forwardToAll(event);
	} else {
	    // System.out.println(getId() + " Skipping " + event);
	}
    }

    protected EventRelay getRelayForPeer(String peerId) {
	/* check direct peers */
	EventRelay peerRelay = getDirectPeerRelay(peerId);
	if (peerRelay == null) {
	    /* check remote peers */
	    peerRelay = getRemotePeerRelay(peerId);
	}

	return peerRelay;
    }

    protected void forwardTo(Event event, Set<String> peerIds) {
	/* check if if we are targeted by the event as well */
	if(peerIds.contains(getId())) {
	    postInternally(event);
	    return;
	}

	/* only forward further, if there are other targeted nodes except us */
	if (peerIds.size() <= 0) {
	    return;
	}
	
	/* Keep the peers indexed by the next hop in the path to them */
	SetMap<String, String> peersByVia = new SetMap<>();

	EventContext ec = new EventContext(event, null);
	for (String cto : peerIds) {
	    String cvia = null;
	    /* if this is a direct peer or us, use targeted peer id as via */
	    if (getDirectPeerRelay(cto) != null || getId().equals(cto)) {
		cvia = cto;
	    } else {
		/* if it's a remote peer, we should have a via in the routing table */
		cvia = routingTable.getBestViaTo(cto);
	    }

	    if (cvia == null) {
		/*
		 * if we can't find a via for a remote node, then forward to all, however this should not happen under
		 * normal conditions
		 */
		System.err.println(getId() + ": Couldn't find a via for " + cto + " , forwarding to all");
		forwardToAll(event);
		return;
	    } else {
		peersByVia.add(cvia, cto);
	    }
	}

	/* now we have to send an event to each via */
	for (Map.Entry<String, Set<String>> re : peersByVia.entrySet()) {
	    /*
	     * we have to create a new event with the header field forwardTo updated with the peers that we can reach
	     * through each via
	     */

	    /* if we want to forward messages via this relay, we have to add the nodes in the forwardTo field */
	    String via = re.getKey();

	    Set<String> ftp = peersByVia.get(via);
	    if (ftp != null) {
		/* if our rules forbid us to forward to this node, then don't bother */
		if (!isEventForwardingAllowed(ec, via)) {
		    System.out.println(getId() + ": Not allowed forwarding via " + via + " " + event);
		    continue;
		}
		EventRelay viaRelay = getDirectPeerRelay(via);

		/* clone event, rewrite forwardTo and send it directly */
		Event ce = event.clone();
		ce.setForwardTo(ftp);

		/* since this is a forward, we will send directly, regardless of the rules of the peer */
		viaRelay.sendEvent(ce);

	    }
	}
    }

    protected void forwardToAllKnown(Event event) {

	EventContext ec = new EventContext(event, null);
	Stream<String> dpStream = directPeers.entrySet().stream().filter((e) -> e.getValue().isForwardingAllowed(ec))
		.map((e) -> e.getKey());
	Stream<String> rpStream = remotePeers.entrySet().stream().filter((e) -> e.getValue().isForwardingAllowed(ec))
		.map((e) -> e.getKey());
	Set<String> targetedPeers = Stream.concat(dpStream, rpStream).collect(Collectors.toSet());

	forwardTo(event, targetedPeers);
//	event.setForwardTo(targetedPeers);
//	handleRemoteEvent(ec);
    }

    protected static String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    protected EventRelay getDirectPeerRelay(String id) {
	return directPeers.get(id);
    }

    protected EventRelay getRemotePeerRelay(String id) {
	return remotePeers.get(id);
    }

    public boolean isEventForwardingAllowed(EventContext ec, String peerId) {
	Event event = ec.event();

	/* if a destination is specified, only forward to that peerId */
	String destination = event.to();
	if (destination != null && !event.wasRelayedBy(peerId)) {
	    return true;
	}

	/* if this wasn't forwarded allow it */
	String sourceBusId = event.from();
	if (sourceBusId == null) {
	    return true;
	}
	/* don't forward an event to a peer that already relayed that event */
	else if (config.isAutoRelayEnabled() && !event.wasRelayedBy(peerId)) {
	    return true;
	}

	return false;
    }

    protected abstract EventRelay buildLocalRelay(String peerId);

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @return the peers
     */
    protected Map<String, EventRelay> getPeers() {
	return directPeers;
    }

    public void onBindConfirmed(EventRelay localRelay) {
	PeerBindConfirmedEvent pbce = new PeerBindConfirmedEvent(new PeerBindConfirmation(localRelay));
	/* handle this internally */
	forwardTo(pbce, getId());
    }
    

    /**
     * @return the stats
     */
    public EventNodeStats getStats() {
        return stats;
    }






    class EventNodeDispatcher extends SimpleEventDispatcher {

	@Override
	public boolean dispatchEvent(EventContext ec) {
	    stats.onEvent(ec);
	    boolean forUs = handleRemoteEvent(ec);
	    if (forUs) {
		Event event = ec.getEvent();
		/*if no from address, then this comes from us */
		if(event.from() == null) {
		    event.addRelay(getId());
		}
		/* dispatch this further only if this event is meant for us */
		return super.dispatchEvent(ec);
	    }
	    return false;
	}

    }

}