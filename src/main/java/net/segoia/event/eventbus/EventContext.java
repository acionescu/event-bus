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

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This carries an {@link Event} to all the interested {@link EventListener}
 * @author adi
 *
 */
public class EventContext {
    private Event event;
    private EventBus eventBus;
    private Deque<Exception> errorStack;
    /**
     * A listener used to track the lifecycle of an event
     */
    private EventListener lifecycleListener; 

    public EventContext(Event event, EventBus eventBus) {
	super();
	this.event = event;
	this.eventBus = eventBus;
    }
    
    public EventContext(Event event, EventBus eventBus, EventListener lifecycleListener) {
	super();
	this.event = event;
	this.eventBus = eventBus;
	this.lifecycleListener = lifecycleListener;
    }

    public void sendLifecycleEvent(EventContext ec) {
	if(lifecycleListener != null) {
	    ec.visitListener(lifecycleListener);
	}
	else {
	    throw new UnsupportedOperationException("Event context for event "+event.getId()+" has no lifecycle listener defined");
	}
    }

    public void visitListener(EventListener listener) {
	try {
	    listener.onEvent(this);
	} catch (Exception e) {
	    pushError(e);
	    e.printStackTrace();
	}
    }
    
    public boolean hasLifecycleListener() {
	return (lifecycleListener != null);
    }

    private void pushError(Exception e) {
	if (errorStack == null) {
	    initErrorStack();
	}
	errorStack.push(e);
    }

    private void initErrorStack() {
	errorStack = new ArrayDeque<>();
    }
    
    public boolean hasErrors() {
	return (errorStack != null);
    }
    
    public Deque<Exception> getErrorStack(){
	return errorStack;
    }

    public Event event() {
	return event;
    }

    public EventBus bus() {
	return eventBus;
    }

    /**
     * @return the event
     */
    public Event getEvent() {
	return event;
    }

    /**
     * @return the eventBus
     */
    public EventBus getEventBus() {
	return eventBus;
    }

    public EventTypeConfig getConfigForEventType() {
	return eventBus.getConfigForEventType(event.getEt());
    }

    public EventTypeConfig getConfigForEventType(boolean useDefaultIfMissing) {
	return eventBus.getConfigForEventType(event.getEt(), useDefaultIfMissing);
    }

    public EventTypeConfig getConfigForEventType(EventTypeConfig defaultConfig) {
	return eventBus.getConfigForEventType(event.getEt(), defaultConfig);
    }

    /**
     * Convenient method to post an event to the bus
     * 
     * @param event
     * @return
     */
    public InternalEventTracker postEvent(Event event) {
	return eventBus.postEvent(event);
    }
}
