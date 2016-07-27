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
	getRelayForPeer(peerNode, true);
    }
    
    public void registerPeer(EventBusNode peerNode, Condition condition) {
	EventBusRelay localRelay = getRelayForPeer(peerNode, true);
	localRelay.registerForCondition(condition);	
    }
    
    protected EventBusRelay getRelayForPeer(EventBusNode peerNode, boolean create) {
	EventBusRelay localRelay = getPeer(peerNode.getId());

	if (localRelay == null && create) {
	    localRelay = addPeer(peerNode);
	    EventBusRelay remoteRelay = peerNode.addPeer(this);
	    localRelay.bind(remoteRelay);
	    
	    Event nne = Events.builder().ebus().cluster().newNode().topic(getId()).build();
	    
	    nne.addParam("peerId", peerNode.getId());
	    
	    postInternally(nne);
	}
	
	return localRelay;
    }
    
    protected abstract EventTracker postInternally(Event event);

    protected String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    protected EventBusRelay getPeer(String id) {
	return peers.get(id);
    }

    protected EventBusRelay addPeer(EventBusNode peerNode) {
	String peerId = generatePeerId();
	EventBusRelay localRelay = buildLocalRelay(peerId, peerNode);
	peers.put(peerNode.getId(), localRelay);
	localRelay.init();
	
	return localRelay;
    }
    
    public boolean isEventForwardingAllowed(EventContext ec, String peerId) {
	Event event = ec.event();
	/* if this wasn't forwarded allow it */
	if(event.getSourceBusId() == null ) {
	    return true;
	}
	else if(config.isAutoRelayEanbled() && !event.wasRelayedBy(peerId)) {
	    return true;
	}
	
	return false;
    }

    protected abstract EventBusRelay buildLocalRelay(String peerId, EventBusNode peerNode);

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
