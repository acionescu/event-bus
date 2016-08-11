package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;

public abstract class EventRelay {
    protected EventRelay peerRelay;
    private String id;

    protected EventNode parentNode;

    private Condition forwardingCondition;
    private boolean active;

    public EventRelay(String id, EventNode parentNode) {
	this.id = id;
	this.parentNode = parentNode;
    }

    public EventRelay(String id, EventNode parentNode, EventRelay peerRelay) {
	this(id, parentNode);
	this.peerRelay = peerRelay;
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * Connects this relay with a remote relay
     * 
     * @param peerRelay
     */
    public void bind(EventRelay peerRelay) {
	this.peerRelay = peerRelay;
	peerRelay.peerRelay = this;
	parentNode.onBindConfirmed(this);
    }

    public void onLocalEvent(EventContext ec) {
	forwardEvent(ec);
    }

    public void onRemoteEvent(Event event) {
	receiveEvent(event);
    }

    protected void forwardEvent(EventContext ec) {
	if (parentNode.isEventForwardingAllowed(ec, peerRelay.getParentNodeId()) && isForwardingAllowed(ec)) {
	    Event event = ec.event();
	    /* We need to copy this event before sending */
	    sendEvent(event.clone());
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
	parentNode.onRemoteEvent(new PeerEventContext(this, event));
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

    public void bindAccepted(EventNode node) {
	/* only the parent node can call this */
	if (parentNode == node) {
	    doStart();
	}
    }

    private void doStart() {
	if (!active) {
	    active = true;
	    start();
	    peerRelay.onRemoteStarted(this);
	}
    }

    private void onRemoteStarted(EventRelay peerRelay) {
	if (this.peerRelay == peerRelay) {
	    doStart();
	}
    }

    protected abstract void init();

    protected abstract void start();

    public void terminate() {
	if (peerRelay != null) {
	    peerRelay.onRemoteLeaving(this);
	}
	cleanUp();
    }

    protected abstract void cleanUp();

    protected void onRemoteLeaving(EventRelay peerRelay) {
	parentNode.onPeerLeaving(peerRelay.getParentNodeId());
    }

}