package net.segoia.event.eventbus.peers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventTracker;
import net.segoia.event.eventbus.constants.EventParams;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.peers.events.PeerRegisterRequestEvent;
import net.segoia.event.eventbus.peers.events.PeerRegisterRequestEvent.Data;
import net.segoia.event.eventbus.peers.events.PeerRegisteredEvent;
import net.segoia.event.eventbus.peers.events.PeerRequestUnregisterEvent;
import net.segoia.event.eventbus.peers.events.PeerUnregisteredEvent;
import net.segoia.event.eventbus.peers.routing.RoutingTable;
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
    private Map<String, EventRelay> direct = new HashMap<>();

    private Map<String, EventRelay> remotePeers = new HashMap<>();

    private RoutingTable routingTable = new RoutingTable();

    public EventNode() {
	/* generate an id for ourselves */
	this.id = generatePeerId();
	config = new EventBusNodeConfig();
    }

    public EventNode(String id) {
	this();
	this.id = id;
    }

    public EventNode(EventBusNodeConfig config) {
	this();
	this.config = config;
    }

    protected abstract void init();

    public void terminate() {
	/* call terminate on all peer relays */
	for (EventRelay relay : direct.values()) {
	    relay.terminate();
	}

	/* then clean up */
	cleanUp();
    }

    public abstract void cleanUp();

    public void registerPeer(EventNode peerNode) {
	registerPeer(new PeeringRequest(peerNode));
    }

    public void registerPeer(EventNode peerNode, Condition condition) {
	registerPeer(new PeeringRequest(peerNode, condition));
    }

    public void registerPeer(PeeringRequest request) {
	getRelayForPeer(request, true);
    }

    public void unregisterPeer(EventNode peerNode) {
	String peerId = peerNode.getId();
	removePeer(peerId);
	peerNode.removePeer(getId());

	// TODO: handle any errors that appear during removal
	onPeerRemoved(peerId);
    }

    /**
     * Called when a peer has called {@link #terminate()}
     * 
     * @param peerId
     */
    public void onPeerLeaving(String peerId) {
	removePeer(peerId);
	onPeerRemoved(peerId);
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
	EventRelay localRelay = getPeerRelay(peerId);
	if (localRelay != null) {
	    /* call cleanUp not terminate */
	    localRelay.cleanUp();
	}
	direct.remove(peerId);
	routingTable.removeAllFor(peerId);
    }

    protected boolean setRelayForwardingCondition(EventRelay relay, Condition condition) {
	// TODO: check if the condition is allowed by this node
	relay.setForwardingCondition(condition);
	return true;
    }

    protected EventRelay getRelayForPeer(PeeringRequest req, boolean create) {
	EventNode peerNode = req.getRequestingNode();
	EventRelay localRelay = getPeerRelay(peerNode.getId());

	if (localRelay == null && create) {
	    localRelay = addPeer(req);
	    EventRelay remoteRelay = peerNode.addPeer(getPeeringRequest());
	    localRelay.bind(remoteRelay);
	    /* start relays */
	    remoteRelay.start();
	    localRelay.start();

	    onNewPeer(peerNode);
	} else {
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
    protected void onNewPeer(EventNode peerNode) {
	Event nne = Events.builder().ebus().peer().newPeer().topic(getId()).build();
	nne.addParam("peerId", peerNode.getId());
	forwardToAll(nne);
    }

    protected PeeringRequest getPeeringRequest() {
	return new PeeringRequest(this, config.getDefaultRequestedEvents());
    }

    protected EventRelay addPeer(PeeringRequest req) {
	EventNode peerNode = req.getRequestingNode();
	String peerId = generatePeerId();
	EventRelay localRelay = buildLocalRelay(peerId);
	direct.put(peerNode.getId(), localRelay);
	localRelay.init();

	setRelayForwardingCondition(localRelay, req.getEventsCondition());

	return localRelay;
    }

    /**
     * Called for all events, remote or internally generated
     * 
     * @param event
     * @return
     */
    protected abstract EventTracker handleEvent(Event event);

    /**
     * Called for received remote events meant for this node </br>
     * By default it just calls {@link #handleEvent(Event)}
     * 
     * @param pc
     */
    protected void handleRemoteEvent(PeerEventContext pc) {
	handleEvent(pc.getEvent());
    }

    /**
     * Called for all received remote events
     * 
     * @param pc
     */
    public void onRemoteEvent(PeerEventContext pc) {
	Event event = pc.getEvent();
	/* if this event is sent to one of our peers then forward it to them */
	String destination = event.to();
	if (destination != null && !destination.equals(getId())) {
	    forwardTo(event, destination);

	} else {
	    /* make sure we handle peer lifecycle events */
	    handlePeerLifecycle(pc);
	    /* then delegate to other logic */
	    handleRemoteEvent(pc);
	}
    }

    private void handlePeerLifecycle(PeerEventContext pc) {
	Event event = pc.getEvent();
	String et = event.getEt();

	String lastRelay = null;

	switch (et) {
	case "EBUS:PEER:NEW":
	    String peerId = (String) event.getParam(EventParams.peerId);
	    lastRelay = event.getLastRelay();
	    if (lastRelay != null) {
		routingTable.addRoute(peerId, lastRelay);
	    }
	    break;

	case "EBUS:PEER:REMOVED":
	    String removedPeerId = (String) event.getParam(EventParams.peerId);
	    lastRelay = event.getLastRelay();
	    if (lastRelay != null) {
		routingTable.removeAllFor(removedPeerId);
	    }
	    break;
	    
	case "PEER:REQUEST:REGISTER":
	    PeerRegisterRequestEvent prre = (PeerRegisterRequestEvent)event;
	    registerRemotePeer(prre);
	    break;
	    
	case "PEER:REQUEST:UNRESITER":
	    unregisterRemotePeer((PeerRequestUnregisterEvent)event); 
	    break;    
	}

    }
    
    protected synchronized void registerRemotePeer(PeerRegisterRequestEvent event) {
	Data ed = event.getData();
	String peerId = ed.getPeerId();
	EventRelay eventRelay = remotePeers.get(peerId);
	if(eventRelay == null) {
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
	remotePeers.put(peerId,relay);
	relay.init();
	return relay;
    }
    
    protected EventRelay buildRemotePeerRelay(String relayId) {
	return new DefaultEventRelay(relayId, this);
    }
    
    protected void unregisterRemotePeer(PeerRequestUnregisterEvent event) {
	
    }
    
    protected void removeRemotePeer(String peerId) {
	EventRelay relay = remotePeers.remove(peerId);
	if(relay != null) {
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
	if (getId().equals(to)) {
	    handleEvent(event);
	    return;
	}

	EventContext ec = new EventContext(event, null);
	/* first check if the destination is one of the peers */
	EventRelay peerRelay = direct.get(to);
	/* if it is, forward the event */
	if (peerRelay != null) {
	    peerRelay.onLocalEvent(ec);
	}
	/* otherwise, set destination and forward it to the peers */
	else {
	    event.to(to);
	    forwardToAll(event);
	}
    }

    protected void forwardToAllKnown(Event event) {
	/* Keep the peers indexed by the next hop in the path to them */
	SetMap<String, String> peersByVia = new SetMap<>();

	EventContext ec = new EventContext(event, null);
	for (Map.Entry<String, EventRelay> e : remotePeers.entrySet()) {
	    /* forward to a remote peer only if it's interested in this event */
	    if (e.getValue().isForwardingAllowed(ec)) {
		String cto = e.getKey();
		/* get via for each remote peer */
		String cvia = routingTable.getBestViaTo(cto);
		if (cvia == null) {
		    /*
		     * if we can't find a via for a remote node, then forward to all, however this should not happen
		     * under normal conditions
		     */
		    System.err.println(getId() + ": Couldn't find a via for " + cto + " , forwarding to all");
		    forwardToAll(event);
		    return;
		} else {
		    peersByVia.add(cvia, cto);
		}
	    }
	}

	/* now we have to send an event to each via */
	for (Map.Entry<String, EventRelay> re : getPeers().entrySet()) {
	    /*
	     * we have to create a new event with the header field forwardTo updated with the peers that we can reach
	     * through each via
	     */

	    // TODO: implement this

	    /* if we want to forward messages via this relay, we have to add the nodes in the forwardTo field */
	    String via = re.getKey();

	    Set<String> ftp = peersByVia.get(via);
	    if (ftp != null) {
		/* if our rules forbid us to forward to this node, then don't bother */
		if (!isEventForwardingAllowed(ec, via)) {
		    System.out.println(getId() + ": Skip forwarding " + event.getEt() + " to via " + via);
		    continue;
		}

		Event ce = event.clone();
		ce.setForwardTo(ftp);
		/* since this is a forward, we will send directly, regardless of the rules of the peer */
		re.getValue().sendEvent(ce);
	    } else {
		/* if we don't have any forwarding to do through this peer, then send as usual, without copying */
		re.getValue().forwardEvent(ec);
	    }
	}

    }

    protected String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    protected EventRelay getPeerRelay(String id) {
	return direct.get(id);
    }

    public boolean isEventForwardingAllowed(EventContext ec, String peerId) {
	Event event = ec.event();

	/* if a destination is specified, only forward to that peerId */
	String destination = event.to();
	if (destination != null && !peerId.equals(destination)) {
	    return false;
	}

	/* if this wasn't forwarded allow it */
	String sourceBusId = event.from();
	if (sourceBusId == null ) {
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
	return direct;
    }

}