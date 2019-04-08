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
package net.segoia.event.eventbus.app;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.GlobalAgentEventNodeContext;
import net.segoia.event.eventbus.util.EBus;

public class EventNodeControllerContext {
    private GlobalAgentEventNodeContext globalContext;
    private String peerId;

    public EventNodeControllerContext(GlobalAgentEventNodeContext globalContext, String peerId) {
	super();
	this.globalContext = globalContext;
	this.peerId = peerId;
    }
    
    /**
     * Send an event to the peer we're servicing
     * @param event
     */
    public void sendToClient(Event event) {
	globalContext.forwardTo(event, peerId);
    }
    
    /**
     * Post an event on the local node
     * @param event
     */
    public void postEvent(Event event) {
	
	globalContext.postEvent(event);
	
	EBus.postSystemEvent(event);
    }

    public String getPeerId() {
        return peerId;
    }
    
    
}
