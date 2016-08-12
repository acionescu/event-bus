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

import net.segoia.event.conditions.TrueCondition;

/**
 * An agent is a node that reacts to events in a certain way </br>
 * It can also post events as a public node, or hiding behind a relay node
 * 
 * @author adi
 *
 */
public abstract class AgentNode extends EventNode {
    protected EventNode mainNode;
  
    
    public AgentNode() {
	this(true);
    }

    public AgentNode(boolean autoinit) {
	super(autoinit);
    }

    public AgentNode(EventBusNodeConfig config) {
	super(config);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#nodeInit()
     */
    @Override
    protected void nodeInit() {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#nodeConfig()
     */
    @Override
    protected void nodeConfig() {
	// TODO Auto-generated method stub
	
    }

    protected void setRequestedEventsCondition() {
	// TODO: implement this, for now request all
	config.setDefaultRequestedEvents(new TrueCondition());
    }

    @Override
    protected EventRelay buildLocalRelay(String peerId) {
	return new DefaultEventRelay(peerId, this);
    }


    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.event.eventbus.peers.EventNode#onPeerLeaving(java.lang.String)
     */
    @Override
    public void onPeerLeaving(String peerId) {
	super.onPeerLeaving(peerId);
	/* since an agent is bound by the main node, if that leaves we need to finish too */
	terminate();
    }

}
