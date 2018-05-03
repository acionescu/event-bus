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
package net.segoia.event.eventbus;


import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.LooseEventMatchCondition;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.peers.EventNode;
import net.segoia.event.eventbus.peers.core.EventTransceiver;

public class PeerBindRequest {
    private transient EventTransceiver transceiver;
	
    /**
     * On what kind of events this node wants to listed </br>
     * If left null, it will attempt to listen on all events </br>
     * it may be rejected if the source node does not allow this kind of peering </br>
     * By default it's set to listen only on EBUS:PEER: events
     */
    private Condition eventsCondition = LooseEventMatchCondition.build(Events.SCOPE.EBUS, Events.CATEGORY.PEER);

    /**
     * If this is true, then the events meant for the master node will be forwarded to this node as well
     */
    private boolean agent;

    /**
     * if this is true, the {@link EventNode#registerPeer(PeeringRequest) will not return until the node was
     * successfully registered *
     */
    private boolean synchronous;

    public PeerBindRequest(EventTransceiver transceiver) {
	super();
	this.transceiver = transceiver;
    }

    public PeerBindRequest(EventTransceiver transceiver, Condition eventsCondition) {
	super();
	this.transceiver = transceiver;
	this.eventsCondition = eventsCondition;
    }

    public PeerBindRequest(EventTransceiver transceiver, Condition eventsCondition, boolean agent) {
	super();
	this.transceiver = transceiver;
	this.eventsCondition = eventsCondition;
	this.agent = agent;
    }

    public PeerBindRequest(EventTransceiver transceiver, boolean synchronous) {
	super();
	this.transceiver = transceiver;
	this.synchronous = synchronous;
    }

    /**
     * @return the eventsCondition
     */
    public Condition getEventsCondition() {
	return eventsCondition;
    }

    

    public EventTransceiver getTransceiver() {
        return transceiver;
    }

    /**
     * @param eventsCondition
     *            the eventsCondition to set
     */
    public void setEventsCondition(Condition eventsCondition) {
	this.eventsCondition = eventsCondition;
    }

    /**
     * @return the agent
     */
    public boolean isAgent() {
	return agent;
    }

    public boolean isSynchronous() {
	return synchronous;
    }

    public void setSynchronous(boolean synchronous) {
	this.synchronous = synchronous;
    }

}
