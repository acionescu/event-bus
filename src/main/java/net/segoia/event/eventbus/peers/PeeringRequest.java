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
import net.segoia.event.conditions.LooseEventMatchCondition;
import net.segoia.event.eventbus.constants.Events;

public class PeeringRequest {
    private EventNode requestingNode;
    /**
     * On what kind of events this node wants to listed
     * </br>
     * If left null, it will attempt to listen on all events
     * </br>
     * it may be rejected if the source node does not allow this kind of peering
     * </br>
     * By default it's set to listen only on EBUS:PEER: events
     */
    private Condition eventsCondition=LooseEventMatchCondition.build(Events.SCOPE.EBUS,Events.CATEGORY.PEER);
    
    public PeeringRequest(EventNode requestingNode) {
	super();
	this.requestingNode = requestingNode;
    }
    public PeeringRequest(EventNode requestingNode, Condition eventsCondition) {
	super();
	this.requestingNode = requestingNode;
	this.eventsCondition = eventsCondition;
    }
    /**
     * @return the requestingNode
     */
    public EventNode getRequestingNode() {
        return requestingNode;
    }
    /**
     * @return the eventsCondition
     */
    public Condition getEventsCondition() {
        return eventsCondition;
    }
    /**
     * @param requestingNode the requestingNode to set
     */
    public void setRequestingNode(EventNode requestingNode) {
        this.requestingNode = requestingNode;
    }
    /**
     * @param eventsCondition the eventsCondition to set
     */
    public void setEventsCondition(Condition eventsCondition) {
        this.eventsCondition = eventsCondition;
    }
    
}
