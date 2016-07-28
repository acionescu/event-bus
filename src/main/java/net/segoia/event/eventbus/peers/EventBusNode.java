package net.segoia.event.eventbus.peers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventTracker;
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

    private Map<String, EventBusRelay> peers = new HashMap<>();

    public EventBusNode() {
	/* generate an id for ouselves */
	this.id = generatePeerId();
    }

    public EventBusNode(String id) {
	super();
	this.id = id;
    }

    public void registerPeer(EventBusNode peerNode) {
	registerPeer(new PeeringRequest(peerNode));
    }

    public void registerPeer(EventBusNode peerNode, Condition condition) {
	registerPeer(new PeeringRequest(peerNode,condition));
    }

    public void registerPeer(PeeringRequest request) {
	 getRelayForPeer(request, true);
    }
    
    protected boolean setRelayForwardingCondition(EventBusRelay relay, Condition condition) {
	//TODO: check if the condition is allowed by this node
	 relay.setForwardingCondition(condition);
	 return true;
    }

    protected EventBusRelay getRelayForPeer(PeeringRequest req, boolean create) {
	EventBusNode peerNode = req.getRequestingNode();
	EventBusRelay localRelay = getPeer(peerNode.getId());

	if (localRelay == null && create) {
	    localRelay = addPeer(req);
	    EventBusRelay remoteRelay = peerNode.addPeer(new PeeringRequest(this));
	    localRelay.bind(remoteRelay);

	    Event nne = Events.builder().ebus().peer().newPeer().topic(getId()).build();

	    nne.addParam("peerId", peerNode.getId());

	    postInternally(nne);
	}
	else {
	    /* if the relay exists, check it the condition has changed */
	    setRelayForwardingCondition(localRelay, req.getEventsCondition());
	}

	return localRelay;
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

    protected String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    protected EventBusRelay getPeer(String id) {
	return peers.get(id);
    }

    public boolean isEventForwardingAllowed(EventContext ec, String peerId) {
	Event event = ec.event();
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

}

/*
 * An event can be : public - sent all peers shared - sent only to selected peers private - not sent to peers
 */
