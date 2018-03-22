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

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequest;

public abstract class EventRelay {
    protected EventTransceiver transceiver;
    private String id;
    
    private EventNode parentNode;
    
    private Condition forwardingCondition;
    
    /**
     * the request from which this relay was created 
     */
    private PeerBindRequest peeringRequest;
    
    private boolean active;

    public EventRelay(String id,EventNode parentNode) {
	this.id = id;
	this.parentNode = parentNode;
    }

    public EventRelay(String id, EventNode parentNode, EventTransceiver tranceiver) {
	this(id, parentNode);
	this.transceiver = tranceiver;
	
    }
    
    public EventRelay(EventNode parentNode, EventTransceiver tranceiver) {
	this.parentNode = parentNode;
	this.transceiver = tranceiver;
	
    }
    
    public String getChannel() {
	return transceiver.getChannel();
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    public void bind() {
	transceiver.setActive(this);
    }
    
    /**
     * Connects this relay with a remote relay
     * 
     * @param peerRelay
     */
    public void bind(EventRelay peerRelay) {
	this.transceiver = peerRelay;
	peerRelay.transceiver = this;
	parentNode.onBindConfirmed(this);
    }

    public void onLocalEvent(EventContext ec) {
	forwardEvent(ec);
    }

    public void onRemoteEvent(Event event) {
	receiveEvent(event);
    }

    protected void forwardEvent(EventContext ec) {
	if (parentNode.isEventForwardingAllowed(ec, transceiver.getParentNodeId()) && isForwardingAllowed(ec)) {
	    Event event = ec.event();
	    /* We need to copy this event before sending */
	    sendEvent(event.clone());
	}
	else {
	    System.out.println("Discarding event: "+ec.getEvent());
	}
    }

    protected boolean isForwardingAllowed(EventContext ec) {
	return (forwardingCondition != null && forwardingCondition.test(ec));
    }

    protected void sendEvent(Event event) {
	event.addRelay(getParentNodeId());
	transceiver.sentEvent(event);
    }

    protected void receiveEvent(Event event) {
	event.addRelay(getId());
	parentNode.onRemoteEvent(new PeerEventContext(this, event));
    }

    protected void setForwardingCondition(Condition condition) {
	forwardingCondition = condition;
    }
    
    
    

    /**
     * @return the peeringRequest
     */
    public PeerBindRequest getPeeringRequest() {
        return peeringRequest;
    }

    /**
     * @param peeringRequest the peeringRequest to set
     */
    public void setPeeringRequest(PeerBindRequest peeringRequest) {
        this.peeringRequest = peeringRequest;
        if(peeringRequest != null) {
            setForwardingCondition(peeringRequest.getEventsCondition());
        }
    }
    
    public boolean isRemoteNodeAgent() {
	if(peeringRequest != null) {
	    return peeringRequest.isAgent();
	}
	return false;
    }

    public String getParentNodeId() {
	return parentNode.getId();
    }

    public String getRemoteNodeId() {
	return transceiver.getParentNodeId();
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
	    transceiver.onRemoteStarted(this);
	}
    }

    private void onRemoteStarted(EventRelay peerRelay) {
	if (this.transceiver == peerRelay) {
	    doStart();
	}
    }

    protected abstract void init();

    protected abstract void start();

    public void terminate() {
	if (transceiver != null) {
	    transceiver.onRemoteLeaving(this);
	}
	cleanUp();
    }

    protected abstract void cleanUp();

    protected void onRemoteLeaving(EventRelay peerRelay) {
	parentNode.onPeerLeaving(peerRelay.getParentNodeId());
    }

    public PeerEventListener getPeerEventListener() {
        return peerEventListener;
    }

    public void setPeerEventListener(PeerEventListener peerEventListener) {
        this.peerEventListener = peerEventListener;
    }

}
