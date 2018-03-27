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
import net.segoia.event.eventbus.EventListener;

/**
 * Implements a certain communication protocol over an {@link EventTransceiver}
 * 
 * @author adi
 *
 */
public abstract class EventRelay extends AbstractEventTransceiver implements EventListener {
    protected EventTransceiver transceiver;
    private String id;

    private Condition forwardingCondition;

    public EventRelay(String id) {
	this.id = id;
    }

    public EventRelay(String id, EventTransceiver transceiver) {
	this(id);
	this.transceiver = transceiver;

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

	transceiver.setRemoteEventListener(this);
    }
    //
    // public void onLocalEvent(EventContext ec) {
    // forwardEvent(ec);
    // }

    public void onEvent(Event event) {
	receiveEvent(event);
    }

    // protected void forwardEvent(EventContext ec) {
    // if (parentNode.isEventForwardingAllowed(ec, transceiver.getParentNodeId()) && isForwardingAllowed(ec)) {
    // Event event = ec.event();
    // /* We need to copy this event before sending */
    // sendEvent(event.clone());
    // } else {
    // System.out.println("Discarding event: " + ec.getEvent());
    // }
    // }

    protected boolean isForwardingAllowed(EventContext ec) {
	return (forwardingCondition != null && forwardingCondition.test(ec));
    }

    public void sendEvent(Event event) {
	event.addRelay(getParentNodeId());
	transceiver.sendEvent(event);
    }

    public void receiveEvent(Event event) {
	event.addRelay(getId());
	getRemoteEventListener().onEvent(event);
    }

    protected void setForwardingCondition(Condition condition) {
	forwardingCondition = condition;
    }

    // /**
    // * @param peeringRequest
    // * the peeringRequest to set
    // */
    // public void setPeeringRequest(PeerBindRequest peeringRequest) {
    // this.peeringRequest = peeringRequest;
    // if (peeringRequest != null) {
    // setForwardingCondition(peeringRequest.getEventsCondition());
    // }
    // }

    public String getParentNodeId() {
	return parentNode.getId();
    }

    // public void bindAccepted(EventNode node) {
    // /* only the parent node can call this */
    // if (parentNode == node) {
    // doStart();
    // }
    // }

    // private void doStart() {
    // if (!active) {
    // active = true;
    // start();
    // transceiver.onRemoteStarted(this);
    // }
    // }

    // private void onRemoteStarted(EventRelay peerRelay) {
    // if (this.transceiver == peerRelay) {
    // doStart();
    // }
    // }

    public void start() {
	transceiver.init();
    }

    public void terminate() {
	if (transceiver != null) {
	    transceiver.terminate();
	}
	cleanUp();
    }

    protected abstract void cleanUp();

    protected void onRemoteLeaving(EventRelay peerRelay) {
	parentNode.onPeerLeaving(peerRelay.getParentNodeId());
    }

}
