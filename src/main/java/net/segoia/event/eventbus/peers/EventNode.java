package net.segoia.event.eventbus.peers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventTracker;
import net.segoia.event.eventbus.constants.EventParams;
import net.segoia.event.eventbus.constants.Events;

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
    private Map<String, EventRelay> peers = new HashMap<>();

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
	for(EventRelay relay : peers.values()) {
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
	peers.remove(peerId);
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
	peers.put(peerNode.getId(), localRelay);
	localRelay.init();

	setRelayForwardingCondition(localRelay, req.getEventsCondition());

	return localRelay;
    }

    /**
     * Called for all events, remote or internally generated
     * @param event
     * @return
     */
    protected abstract EventTracker handleEvent(Event event);

    
    /**
     * Called for received remote events meant for this node
     * </br>
     * By default it just calls {@link #handleEvent(Event)}
     * @param pc
     */
    protected void handleRemoteEvent(PeerEventContext pc) {
	handleEvent(pc.getEvent());
    }
    
    /**
     * Called for all received remote events
     * @param pc
     */
    public void onRemoteEvent(PeerEventContext pc) {
	Event event = pc.getEvent();
	/* if this event is sent to one of our peers then forward it to them */
	String destination = event.to();
	if(destination != null && !destination.equals(getId())) {
	    forwardTo(event, destination);
	    
	}
	else {
	    handleRemoteEvent(pc);
	}
    }
    
    protected void forwardToAll(Event event) {
	EventContext ec = new EventContext(event, null);
	for(EventRelay r : getPeers().values()) {
	    r.onLocalEvent(ec);
	}
    }
    
    protected void forwardTo(Event event, String to) {
	if(getId().equals(to)) {
	    handleEvent(event);
	    return;
	}
	
	EventContext ec = new EventContext(event, null);
	/* first check if the destination is one of the peers */
	EventRelay peerRelay = peers.get(to);
	/* if it is, forward the event */
	if(peerRelay != null) {
	    peerRelay.onLocalEvent(ec);
	}
	/* otherwise, set destination and forward it to the peers */
	else {
	    event.to(to);
	    forwardToAll(event);
	}
    }
    
    protected String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    protected EventRelay getPeerRelay(String id) {
	return peers.get(id);
    }

    public boolean isEventForwardingAllowed(EventContext ec, String peerId) {
	Event event = ec.event();
	
	/* if a destination is specified, only forward to that peerId */
	String destination = event.to();
	if( destination != null && !peerId.equals(destination)) {
	    return false;
	}
	
	/* if this wasn't forwarded allow it */
	String sourceBusId = event.from();
	if (sourceBusId == null || sourceBusId.equals(id)) {
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
	return peers;
    }

}