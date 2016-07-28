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
    
    public void bind(EventBusRelay peerRelay) {
	this.peerRelay = peerRelay;
	peerRelay.peerRelay = this;
    }
    
    
    protected void onLocalEvent(EventContext ec) {
	forwardEvent(ec);
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
    
    protected void onRemoteEvent(Event event) {
	receiveEvent(event);
    }
    
    protected void receiveEvent(Event event) {
	postInternally(event);
    }
    
    protected abstract void postInternally(Event event);
    
    protected void setForwardingCondition(Condition condition) {
	forwardingCondition = condition;
    }
    
    public String getParentNodeId() {
	return parentNode.getId();
    }
    
    protected abstract void init();
    
    protected abstract void terminate();
}
