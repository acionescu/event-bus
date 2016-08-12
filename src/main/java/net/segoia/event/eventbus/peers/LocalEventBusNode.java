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
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.InternalEventTracker;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.util.EBus;

public class LocalEventBusNode extends EventNode{
    private FilteringEventBus bus;
    
    public LocalEventBusNode() {
	this(EBus.instance());
    }

    public LocalEventBusNode(FilteringEventBus bus) {
	this.bus = bus;
    }

    public LocalEventBusNode(FilteringEventBus bus, EventBusNodeConfig config) {
	super(config);
	this.bus = bus;
    }
    
    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#registerHandlers()
     */
    @Override
    protected void registerHandlers() {
	super.registerHandlers();
	/* register a generic handler that will post all events to the bus */
	addEventHandler((c) -> this.handleEvent(c.getEvent()));
    }
    
    

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#handleRemoteEvent(net.segoia.event.eventbus.EventContext)
     */
    @Override
    protected boolean handleRemoteEvent(EventContext pc) {
	/* we want to peek on all events */
	handleEvent(pc.getEvent());
	return super.handleRemoteEvent(pc);
    }
    
    

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#forwardToAll(net.segoia.event.eventbus.Event)
     */
    @Override
    protected void forwardToAll(Event event) {
	if(event.from() == null) {
	    /* if it comes from us, peek */
	    handleEvent(event);
	}
	super.forwardToAll(event);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#forwardTo(net.segoia.event.eventbus.Event, java.lang.String)
     */
    @Override
    protected void forwardTo(Event event, String to) {
	if(event.from() == null) {
	    /* if it comes from us, peek */
	    handleEvent(event);
	}
	super.forwardTo(event, to);
    }

    /**
     * We're simply posting the event to the provided bus
     */
    protected InternalEventTracker handleEvent(Event event) {
	InternalEventTracker eventTracker = bus.postEvent(event);
	if(eventTracker.hasErrors()) {
	    eventTracker.getFirstError().printStackTrace();
	}
	return eventTracker;
    }
    
    
    protected void setRequestedEventsCondition() {
   	config.setDefaultRequestedEvents(new TrueCondition());
       }

       @Override
       protected EventRelay buildLocalRelay(String peerId) {
   	return new DefaultEventRelay(peerId, this);
       }

    
    @Override
    public void cleanUp() {
	
    }

    @Override
    protected void onTerminate() {
	// TODO Auto-generated method stub
	
    }

    @Override
    protected void nodeInit() {
	// TODO Auto-generated method stub
	
    }

    @Override
    protected void nodeConfig() {
	// TODO Auto-generated method stub
	
    }

    
    
}
