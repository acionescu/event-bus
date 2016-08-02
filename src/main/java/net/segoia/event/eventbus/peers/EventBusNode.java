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
public abstract class EventBusNode {
    private String id;
    private EventBusNodeConfig config = new EventBusNodeConfig();

    /**
     * keep the relays associated with peers
     */
    private Map<String, EventBusRelay> peers = new HashMap<>();

    public EventBusNode() {
	/* generate an id for ourselves */
	this.id = generatePeerId();
    }

    public EventBusNode(String id) {
	super();
	this.id = id;
    }

    public EventBusNode(EventBusNodeConfig config) {
	this();
	this.config = config;
    }
    
    protected abstract void init();
    
    public abstract void terminate();

    public void registerPeer(EventBusNode peerNode) {
	registerPeer(new PeeringRequest(peerNode));
    }

    public void registerPeer(EventBusNode peerNode, Condition condition) {
	registerPeer(new PeeringRequest(peerNode, condition));
    }

    public void registerPeer(PeeringRequest request) {
	getRelayForPeer(request, true);
    }

    public void unregisterPeer(EventBusNode peerNode) {
	removePeer(peerNode);
	peerNode.removePeer(this);

	// TODO: handle any errors that appear during removal
	onPeerRemoved(peerNode);
    }

    /**
     * Called after a peer has been unregistered </br>
     * By default sends an EBUS:PEER:REMOVED message </br>
     * Override this if you want specific behavior
     * 
     * @param peerNode
     */
    protected void onPeerRemoved(EventBusNode peerNode) {
	Event nne = Events.builder().ebus().peer().peerRemoved().topic(getId()).build();
	nne.addParam(EventParams.peerId, peerNode.getId());
	postInternally(nne);
    }

    protected void removePeer(EventBusNode peerNode) {
	EventBusRelay localRelay = getPeerRelay(peerNode.getId());
	if (localRelay != null) {
	    localRelay.terminate();
	}
	peers.remove(peerNode.getId());
    }

    protected boolean setRelayForwardingCondition(EventBusRelay relay, Condition condition) {
	// TODO: check if the condition is allowed by this node
	relay.setForwardingCondition(condition);
	return true;
    }

    protected EventBusRelay getRelayForPeer(PeeringRequest req, boolean create) {
	EventBusNode peerNode = req.getRequestingNode();
	EventBusRelay localRelay = getPeerRelay(peerNode.getId());

	if (localRelay == null && create) {
	    localRelay = addPeer(req);
	    EventBusRelay remoteRelay = peerNode.addPeer(getPeeringRequest());
	    localRelay.bind(remoteRelay);

	    onNewPeer(peerNode);
	} else {
	    /* if the relay exists, check it the condition has changed */
	    setRelayForwardingCondition(localRelay, req.getEventsCondition());
	}

	return localRelay;
    }

    /**
     * Called after a new peer has been registered </br>
     * By default sends an EBUS:PEER:NEW message </br>
     * Override this if you want specific behavior
     * 
     * @param peerNode
     */
    protected void onNewPeer(EventBusNode peerNode) {
	Event nne = Events.builder().ebus().peer().newPeer().topic(getId()).build();
	nne.addParam("peerId", peerNode.getId());
	postInternally(nne);
    }

    protected PeeringRequest getPeeringRequest() {
	return new PeeringRequest(this, config.getDefaultRequestedEvents());
    }

    protected EventBusRelay addPeer(PeeringRequest req) {
	EventBusNode peerNode = req.getRequestingNode();
	String peerId = generatePeerId();
	EventBusRelay localRelay = buildLocalRelay(peerId);
	peers.put(peerNode.getId(), localRelay);
	localRelay.init();

	setRelayForwardingCondition(localRelay, req.getEventsCondition());

	return localRelay;
    }

    protected abstract EventTracker postInternally(Event event);

    
    protected abstract void handleRemoteEvent(PeerEventContext pc);
    
    protected void forwardToAll(Event event) {
	EventContext ec = new EventContext(event, null);
	for(EventBusRelay r : getPeers().values()) {
	    r.onLocalEvent(ec);
	}
    }
    
    
    protected String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    protected EventBusRelay getPeerRelay(String id) {
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
	String sourceBusId = event.sourceBusId();
	if (sourceBusId == null || sourceBusId.equals(id)) {
	    return true;
	}
	/* don't forward an event to a peer that already relayed that event */
	else if (config.isAutoRelayEanbled() && !event.wasRelayedBy(peerId)) {
	    return true;
	}

	return false;
    }

    protected abstract EventBusRelay buildLocalRelay(String peerId);

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @return the peers
     */
    protected Map<String, EventBusRelay> getPeers() {
	return peers;
    }

}

/*
 * An event can be : public - sent all peers shared - sent only to selected peers private - not sent to peers
 */
