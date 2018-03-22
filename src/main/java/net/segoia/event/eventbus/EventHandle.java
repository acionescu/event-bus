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

public class EventHandle {
    private EventContext eventContext;
    private EventRights eventRights;
    private Event event;
    private EventBus bus;

    public EventHandle(EventBus bus, EventContext eventContext, EventRights eventRights) {
	super();
	this.eventContext = eventContext;
	this.eventRights = eventRights;
	this.event = eventContext.event();
	this.bus=bus;
    }

    public boolean isAllowed() {
	return eventRights.isAllowed();
    }

    public InternalEventTracker post() {
	return bus.postEvent(eventContext, this);
    }
    
    public InternalEventTracker send(String to) {
	event.to(to);
	return post();
    }

    /**
     * @return the eventRights
     */
    public EventRights getEventRights() {
	return eventRights;
    }

    public Event getEvent() {
	return event;
    }

    public Event event() {
	return event;
    }
    
    public EventHandle addParam(String key, Object value) {
	event.addParam(key, value);
	return this;
    }

    public EventBus getBus() {
        return bus;
    }
    
    
}
