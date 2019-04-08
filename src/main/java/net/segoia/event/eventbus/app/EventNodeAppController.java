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

import java.util.HashMap;
import java.util.Map;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.FilteringEventProcessor;
import net.segoia.event.eventbus.PassthroughCustomEventContextListenerFactory;
import net.segoia.event.eventbus.peers.CustomEventHandler;

public abstract class EventNodeAppController<C extends EventNodeControllerContext> {
    private EventNodeAppControllerConfig config = new EventNodeAppControllerConfig();
    protected C controllerContext;
    private FilteringEventProcessor eventsProcessor = new FilteringEventProcessor(
	    new PassthroughCustomEventContextListenerFactory());
    
    private Map<String, EventNodeAppController<? extends EventNodeControllerContext>> nestedAppControllers=new HashMap<>();

    public EventNodeAppController() {
	super();
    }

    public EventNodeAppController(EventNodeAppControllerConfig config) {
	super();
	this.config = config;
    }

    public EventNodeAppController(C controllerContext, EventNodeAppControllerConfig config) {
	super();
	this.controllerContext = controllerContext;
	this.config = config;
    }

    public EventNodeAppController(C controllerContext) {
	super();
	this.controllerContext = controllerContext;

    }

    protected abstract void registerEventHandlers();

    public void init(C controllerContext) {
	this.controllerContext = controllerContext;
	registerEventHandlers();
    }

    protected void sendEventToClient(Event event) {
	controllerContext.sendToClient(event);
    }

    protected void postEvent(Event event) {
	controllerContext.postEvent(event);
    }
    
    protected void sendToClientAndPost(Event event) {
	controllerContext.sendToClient(event);
	controllerContext.postEvent(event);
    }

    public EventNodeAppControllerConfig getConfig() {
	return config;
    }

    public void setConfig(EventNodeAppControllerConfig config) {
	this.config = config;
    }

    public void processEvent(EventContext ec) {
	eventsProcessor.processEvent(ec);
	
	for(EventNodeAppController nac : nestedAppControllers.values()) {
	    nac.processEvent(ec);
	}
    }

    protected <E extends Event> void addEventHandler(Class<E> eventClass, CustomEventHandler<E> handler) {
	eventsProcessor.addEventHandler(eventClass, handler);
    }

    protected <E extends Event> void addEventHandler(CustomEventHandler<E> handler) {
	eventsProcessor.addEventHandler(handler);
    }
    
    protected <E extends Event> void addEventHandler(CustomEventHandler<E> handler, int priority) {
   	eventsProcessor.addEventHandler(handler,priority);
       }

    protected void addEventHandler(String eventType, CustomEventHandler<Event> handler) {
	eventsProcessor.addEventHandler(eventType, handler);
    }
    
    protected <E extends Event> void addEventHandler(Condition cond, CustomEventHandler<E> handler) {
	eventsProcessor.addEventHandler(cond, handler);
    }
    
    public void addNestedController(String id, EventNodeAppController<? extends EventNodeControllerContext> controller) {
	nestedAppControllers.put(id, controller);
    }
    
    public void removeNestedController(String id) {
	nestedAppControllers.remove(id);
    }
}
