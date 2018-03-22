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
package net.segoia.event.eventbus.peers.events.register;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.events.register.PeerRegisterRequestEvent.Data;

@EventType("PEER:REQUEST:REGISTER")
public class PeerRegisterRequestEvent extends CustomEvent<Data>{
    
    public PeerRegisterRequestEvent(String peerId, Condition eventsCondition) {
	super(PeerRegisterRequestEvent.class);
	this.data = new Data(peerId, eventsCondition);
    }

    public class Data{
	/**
	 * Id of the peer to be registered
	 */
	private String peerId;
	
	/**
	 * Only events respecting this condition should be forwarded
	 */
	private Condition eventsCondition;

	public Data(String peerId, Condition eventsCondition) {
	    super();
	    this.peerId = peerId;
	    this.eventsCondition = eventsCondition;
	}

	/**
	 * @return the peerId
	 */
	public String getPeerId() {
	    return peerId;
	}

	/**
	 * @return the eventsCondition
	 */
	public Condition getEventsCondition() {
	    return eventsCondition;
	}

	/**
	 * @param peerId the peerId to set
	 */
	public void setPeerId(String peerId) {
	    this.peerId = peerId;
	}

	/**
	 * @param eventsCondition the eventsCondition to set
	 */
	public void setEventsCondition(Condition eventsCondition) {
	    this.eventsCondition = eventsCondition;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("Data [");
	    if (peerId != null)
		builder.append("peerId=").append(peerId).append(", ");
	    if (eventsCondition != null)
		builder.append("eventsCondition=").append(eventsCondition);
	    builder.append("]");
	    return builder.toString();
	}
    }

}
