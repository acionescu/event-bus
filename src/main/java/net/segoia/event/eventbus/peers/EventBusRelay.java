package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;

public abstract class EventBusRelay {
    private EventBusRelay peerRelay;
    private String id;
    
    private EventBusNode parentNode;

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
	if(parentNode.isEventForwardingAllowed(ec, peerRelay.getId())) {
	    Event event = ec.event();
	    event.addRelay(getId());
	    peerRelay.onRemoteEvent(event);
	}
    }
    
    protected void onRemoteEvent(Event event) {
	postInternally(event);
    }
    
    protected abstract void postInternally(Event event);
    
    protected abstract void registerForCondition(Condition condition);

    protected abstract void init();
    
    protected abstract void terminate();
}
