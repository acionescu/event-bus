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
package net.segoia.event.eventbus.peers.test.vo;

import net.segoia.event.eventbus.PeerBindRequest;
import net.segoia.event.eventbus.peers.EventNode;

public class ServerTestEventTransceiver extends TestEventTransceiver{
    /**
     * The event node that will handle this connection
     */
    private EventNode node;
    
    
    public ServerTestEventTransceiver(EventNode node) {
	this.node = node;
    }

    @Override
    public void start() {
	node.registerPeer(new PeerBindRequest(this));
    }

    @Override
    public void init() {
	// TODO Auto-generated method stub
	
    }

}
