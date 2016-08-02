package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;

public abstract class EventBusRelay {
    private EventBusRelay peerRelay;
    private String id;
    
    private EventBusNode parentNode;
    
    private Condition forwardingCondition;

    public EventBusRelay(String id, EventBusNode parentNode) {
	this.id=id;
	this.parentNode=parentNode;
    }


    public EventBusRelay(String id, EventBusNode parentNode, EventBusRelay peerRelay) {
	this(id,parentNode);
	this.peerRelay=peerRelay;
    }
    
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Connects this relay with a remote relay
     * @param peerRelay
     */
    public void bind(EventBusRelay peerRelay) {
	this.peerRelay = peerRelay;
	peerRelay.peerRelay = this;
    }
    
    public void onLocalEvent(EventContext ec) {
	forwardEvent(ec);
    }
    
    protected void onRemoteEvent(Event event) {
	receiveEvent(event);
    }
    
    protected void forwardEvent(EventContext ec) {
	if(parentNode.isEventForwardingAllowed(ec, peerRelay.getParentNodeId()) && isForwardingAllowed(ec)) {
	    sendEvent(ec.event());
	}
    }
    
    protected boolean isForwardingAllowed(EventContext ec) {
	return (forwardingCondition != null && forwardingCondition.test(ec));
    }
    
    protected void sendEvent(Event event) {
	    event.addRelay(getParentNodeId());
	    peerRelay.onRemoteEvent(event);
    }
    
    protected void receiveEvent(Event event) {
	parentNode.handleRemoteEvent(new PeerEventContext(this, event));
    }
    
    
    protected void setForwardingCondition(Condition condition) {
	forwardingCondition = condition;
    }
    
    public String getParentNodeId() {
	return parentNode.getId();
    }
    
    public String getRemoteNodeId() {
	return peerRelay.getParentNodeId();
    }
    
    protected abstract void init();
    
    protected abstract void terminate();
}
