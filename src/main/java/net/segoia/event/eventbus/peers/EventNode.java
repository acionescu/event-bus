/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.eventbus.peers;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.EventClassMatchCondition;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventDispatcher;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.SimpleEventDispatcher;
import net.segoia.event.eventbus.constants.EventParams;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.peers.events.EventNodeInfo;
import net.segoia.event.eventbus.peers.events.NodeTerminateEvent;
import net.segoia.event.eventbus.peers.events.NodeInfo;
import net.segoia.event.eventbus.peers.events.PeerLeavingEvent;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequest;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequestEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAccepted;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAcceptedEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindConfirmation;
import net.segoia.event.eventbus.peers.events.bind.PeerBindConfirmedEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequest;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequestEvent;
import net.segoia.event.eventbus.peers.events.register.PeerRegisterRequestEvent;
import net.segoia.event.eventbus.peers.events.register.PeerRegisterRequestEvent.Data;
import net.segoia.event.eventbus.peers.events.register.PeerRegisteredEvent;
import net.segoia.event.eventbus.peers.events.register.PeerRequestUnregisterEvent;
import net.segoia.event.eventbus.peers.events.register.PeerUnregisteredEvent;
import net.segoia.event.eventbus.peers.security.EventNodeSecurityConfig;
import net.segoia.event.eventbus.peers.security.EventNodeSecurityManager;
import net.segoia.event.eventbus.util.EBus;

/**
 * This encapsulates the connection with another event bus
 * 
 * @author adi
 *
 */
public abstract class EventNode {
    private String id;
    protected EventBusNodeConfig config;

    private NodeInfo nodeInfo;
    private EventNodeStats stats = new EventNodeStats();

    private EventNodeSecurityManager securityManager;

    private PeersManager peersManager;

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

    /**
     * Keep a map with the synchronous registration requests
     */
    private Map<String, PeerBindRequest> waitingRegistration = new Hashtable<>();

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
     * A subclass may implement this to initialize itself
     */
    protected abstract void nodeInit();

    /**
     * A subclass may implement this to set up extra stuff
     */
    protected void nodeConfig() {

	EventNodeSecurityConfig securityConfig = config.getSecurityConfig();

	securityManager = new EventNodeSecurityManager(securityConfig);

	nodeInfo = new NodeInfo(getId());
	nodeInfo.setNodeAuth(securityConfig.getNodeAuth());
	nodeInfo.setSecurityPolicy(securityConfig.getSecurityPolicy());

	peersManager = new PeersManager(this);
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
     * Call this to spawn an extra event bus for this node </br>
     * This may be useful if you want to handle events coming from different sources but still keep the same internal
     * state </br>
     * This additional bus should run in the same thread as the internal bus of this node
     * 
     * @param eventDispatcher
     * @return
     */
    protected FilteringEventBus spawnAdditionalBus(EventDispatcher eventDispatcher) {
	return EBus.buildFilteringEventBusOnMainLoop(eventDispatcher);
    }

    /**
     * Override this to register handlers, but don't forget to call super or you'll lose basic functionality
     */
    protected void registerHandlers() {

	addEventHandler(ConnectToPeerRequestEvent.class, (c) -> {
	    peersManager.handleConnectToPeerRequest(c);
	});

	addEventHandler(PeerBindRequestEvent.class, (c) -> {
	    peersManager.handlePeerBindRequest(c);

	});

	addEventHandler(PeerBindAcceptedEvent.class, (c) -> {
	    peersManager.handlePeerBindAccepted(c);

	});

	addEventHandler(PeerBindConfirmedEvent.class, (c) -> {
	    PeerBindConfirmation data = c.getEvent().getData();
	    EventRelay localRelay = data.getRelay();
	    String peerId = localRelay.getRemoteNodeId();
	    peersRegistry.setDirectPeerRelay(peerId, localRelay);
	    localRelay.bindAccepted(this);
	    onNewPeer(peerId);

	    /* if this is a synchronous registration then notify that it ended */
	    PeerBindRequest r = null;
	    synchronized (waitingRegistration) {
		r = waitingRegistration.remove(peerId);
	    }

	    if (r != null) {
		synchronized (r) {
		    r.notifyAll();
		}
	    }
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
	    for (PeerManager peerContext : peersRegistry.getDirectPeers().values()) {
		peerContext.getRelay().terminate();
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

	/*
	 * if autorelay enabled, then forward the event to the peers marked as agents, that weren't already targeted by
	 * the event
	 */
	addEventHandler((c) -> {
	    Event event = c.getEvent();

	    if (!config.isAutoRelayEnabled() || event.wasRelayedBy(getId())) {
		return;
	    }

	    peersRegistry.getAgents().forEach((peerId) -> {
		if (!event.getForwardTo().contains(peerId) && !peerId.equals(event.to())) {
		    getDirectPeerManager(peerId).onLocalEvent(c);
		}
	    });
	});

    };

    protected abstract void setRequestedEventsCondition();

    public void terminate() {
	forwardTo(new NodeTerminateEvent(this), getId());
    }

    public abstract void cleanUp();

    protected abstract void onTerminate();

    protected void updateRoute(Event event) {
	String from = event.from();
	String via = event.getLastRelay();

	if (from != null && !from.equals(via)) {
	    routingTable.addRoute(from, via);
	}
    }

    public void registerToPeer(ConnectToPeerRequest request) {
	postInternally(new ConnectToPeerRequestEvent(request));
    }

    public void registerPeer(EventNode peerNode) {
	registerPeer(new PeerBindRequest(peerNode));
    }

    public void registerPeer(EventNode peerNode, Condition condition) {
	registerPeer(new PeerBindRequest(peerNode, condition));
    }

    /**
     * Will register peer as an agent
     * 
     * @param peerNode
     * @param condition
     */
    public void registerPeerAsAgent(EventNode peerNode, Condition condition) {
	registerPeer(new PeerBindRequest(peerNode, condition, true));
    }

    public synchronized void registerPeer(PeerBindRequest request) {
	// getRelayForPeer(request, true);
	String peerId = request.getRequestingNode().getId();
	if (request.isSynchronous()) {
	    waitingRegistration.put(peerId, request);
	}

	forwardTo(new PeerBindRequestEvent(request), getId());

	/* if synchronous wait for the registration to finish */
	if (request.isSynchronous()) {
	    PeerBindRequest r = null;
	    synchronized (waitingRegistration) {
		r = waitingRegistration.get(peerId);
	    }

	    if (r != null) {
		synchronized (r) {
		    try {
			r.wait();
		    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    }
	}

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
	EventRelay localRelay = getDirectPeerManager(peerId);
	if (localRelay != null) {
	    /* call cleanUp not terminate */
	    localRelay.cleanUp();
	}
	peersRegistry.removeDirectPeer(peerId);
	routingTable.removeAllFor(peerId);
    }

    protected boolean setRelayForwardingCondition(EventRelay relay, Condition condition) {
	// TODO: check if the condition is allowed by this node
	relay.setForwardingCondition(condition);
	return true;
    }

    protected EventRelay getRelayForPeer(PeerBindRequest req, boolean create) {
	EventNode peerNode = req.getRequestingNode();
	EventRelay localRelay = getDirectPeerManager(peerNode.getId());

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
	if (getDirectPeerManager(peerId).isRemoteNodeAgent()) {
	    peersRegistry.addAgent(peerId);
	    /* don't broadcast agents as peers, this is private business */
	    return;
	}
	Event nne = Events.builder().ebus().peer().newPeer().topic(getId()).build();
	nne.addParam("peerId", peerId);
	forwardToAll(nne);
    }

    protected PeerBindRequest getPeeringRequest() {
	return new PeerBindRequest(this, config.getDefaultRequestedEvents());
    }

    protected EventRelay addPeer(PeerBindRequest req) {
	EventNode peerNode = req.getRequestingNode();
	String peerId = generatePeerId();
	EventRelay localRelay = buildLocalRelay(peerId);
	peersRegistry.setDirectPeerRelay(peerNode.getId(), localRelay);
	localRelay.init();

	setRelayForwardingCondition(localRelay, req.getEventsCondition());

	return localRelay;
    }

    protected EventRelay buildRelay(PeerBindRequest req) {
	String relayId = generatePeerId();
	EventRelay localRelay = buildLocalRelay(relayId);

	localRelay.setPeeringRequest(req);
	localRelay.init();

	return localRelay;
    }

    // /**
    // * Called for all events, remote or internally generated
    // *
    // * @param event
    // * @return
    // */
    // protected abstract EventTracker handleEvent(Event event);

    protected void removeEventHandler(CustomEventListener<?> handler) {
	internalBus.removeListener(handler);
    }

    protected void addEventHandler(Class<?> eventClass, CustomEventListener<?> handler) {
	addBusHandler(new EventClassMatchCondition(eventClass), handler);
    }

    protected void addEventHandler(String eventType, CustomEventListener<?> handler) {
	addBusHandler(new StrictEventMatchCondition(eventType), handler);
    }

    protected <E extends Event> void addEventHandler(Class<E> eventClass, EventHandler<E> handler) {
	addEventHandler(eventClass, new CustomEventListener<>(handler));
    }

    protected <E extends Event> void addEventHandler(String eventType, EventHandler<E> handler) {
	addEventHandler(eventType, new CustomEventListener<>(handler));
    }

    private void addBusHandler(Condition cond, CustomEventListener<?> handler) {
	initInternalBus();
	internalBus.registerListener(cond, handler);
    }

    protected <E extends Event> void addEventHandler(EventHandler<E> handler) {
	addBusHandler(new CustomEventListener<>(handler));
    }

    protected <E extends Event> void addEventHandler(EventHandler<E> handler, int priority) {
	addBusHandler(new CustomEventListener<>(handler), priority);
    }

    private void addBusHandler(CustomEventListener<?> handler) {
	initInternalBus();
	internalBus.registerListener(handler);
    }

    private void addBusHandler(CustomEventListener<?> handler, int priority) {
	initInternalBus();
	internalBus.registerListener(handler, priority);
    }

    public EventNodeSecurityManager getSecurityManager() {
	return securityManager;
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
		if (forwardTo.size() > 0) {
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

	if (internalBus != null) {
	    internalBus.postEvent(event);
	} else {
	    throw new RuntimeException("Warning! Internal bus for node " + getId() + " is not initialized");
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
	eventRelay = peersRegistry.getRemotePeerManager(peerId);
	if (eventRelay == null) {
	    eventRelay = addRemotePeer(peerId);
	}

	eventRelay.setForwardingCondition(ed.getEventsCondition());

	sendPeerRegisteredEvent(peerId);
    }

    /**
     * Override this for extra actions when a peer registers to us
     * 
     * @param peerId
     */
    protected void onPeerRegistered(PeerRegisteredEvent event) {

    }

    private void sendPeerRegisteredEvent(String peerId) {
	PeerRegisteredEvent e = new PeerRegisteredEvent(peerId);
	forwardToAll(e);
	onPeerRegistered(e);
    }

    protected EventRelay addRemotePeer(String peerId) {
	EventRelay relay = buildRemotePeerRelay(generatePeerId());
	peersRegistry.setRemotePeerRelay(peerId, relay);
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
	PeerManager remotePeerContext = peersRegistry.removeRemotePeer(peerId);
	EventRelay relay = remotePeerContext.getRelay();
	if (relay != null) {
	    relay.terminate();
	    sendPeerUnregisteredEvent(peerId);
	}
    }

    /**
     * Override this for extra actions when a peer unregisters from us
     * 
     * @param peerId
     */
    protected void onPeerUnregistered(PeerUnregisteredEvent event) {

    }

    private void sendPeerUnregisteredEvent(String peerId) {
	PeerUnregisteredEvent e = new PeerUnregisteredEvent(peerId);
	forwardToAll(e);
	onPeerUnregistered(e);
    }

    protected abstract EventRelay buildLocalRelay(String peerId);

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    public void onBindConfirmed(EventRelay localRelay) {
	PeerBindConfirmedEvent pbce = new PeerBindConfirmedEvent(new PeerBindConfirmation(localRelay));
	/* handle this internally */
	forwardTo(pbce, getId());
    }

    public EventBusNodeConfig getConfig() {
	return config;
    }

    public void setConfig(EventBusNodeConfig config) {
	this.config = config;
    }

    /**
     * @return the stats
     */
    public EventNodeStats getStats() {
	return stats;
    }

    public NodeInfo getNodeInfo() {
	return nodeInfo;
    }

    class EventNodeDispatcher extends SimpleEventDispatcher {

	@Override
	public boolean dispatchEvent(EventContext ec) {
	    stats.onEvent(ec);
	    boolean forUs = handleRemoteEvent(ec);
	    if (forUs || config.isGod()) {
		Event event = ec.getEvent();
		/* if no from address, then this comes from us */
		if (event.from() == null) {
		    event.addRelay(getId());
		}
		/* dispatch this further only if this event is meant for us */
		return super.dispatchEvent(ec);
	    }
	    return false;
	}

    }

}
