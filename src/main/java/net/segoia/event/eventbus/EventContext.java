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
 * This carries an {@link Event} to all the interested {@link EventContextListener} objects
 * @author adi
 *
 */
public class EventContext {
    private Event event;
    private Deque<Throwable> errorStack;
    /**
     * A listener used to track the lifecycle of an event
     */
    private EventContextListener lifecycleListener; 
    
    /**
     * A way to request the handling of this event by another dispatcher
     */
    private EventDispatcher delegateDispatcher;
    
    private EventHandle eventHandle;

    public EventContext(Event event) {
	super();
	this.event = event;
    }
    
    public EventContext(Event event,  EventContextListener lifecycleListener) {
	super();
	this.event = event;
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

    public void visitListener(EventContextListener listener) {
	try {
	    listener.onEvent(this);
	} catch (Throwable e) {
	    pushError(e);
	    System.err.println(listener.getClass()+" failed processing "+event.getEt()+" with error "+e.getMessage());
	    e.printStackTrace();
	    throw e;
	}
    }
    
    public boolean hasLifecycleListener() {
	return (lifecycleListener != null);
    }

    private void pushError(Throwable e) {
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
    
    public Deque<Throwable> getErrorStack(){
	return errorStack;
    }

    public Event event() {
	return event;
    }

   

    /**
     * @return the event
     */
    public Event getEvent() {
	return event;
    }

   

    public EventTypeConfig getConfigForEventType() {
	return eventHandle.getBus().getConfigForEventType(event.getEt());
    }

    public EventTypeConfig getConfigForEventType(boolean useDefaultIfMissing) {
	return eventHandle.getBus().getConfigForEventType(event.getEt(), useDefaultIfMissing);
    }

    public EventTypeConfig getConfigForEventType(EventTypeConfig defaultConfig) {
	return eventHandle.getBus().getConfigForEventType(event.getEt(), defaultConfig);
    }

    /**
     * Convenient method to post an event to the bus
     * 
     * @param event
     * @return
     */
    public InternalEventTracker postEvent(Event event) {
	return eventHandle.getBus().postEvent(event);
    }

    /**
     * @return the delegateDispatcher
     */
    public EventDispatcher getDelegateDispatcher() {
        return delegateDispatcher;
    }

    /**
     * @param delegateDispatcher the delegateDispatcher to set
     */
    public void setDelegateDispatcher(EventDispatcher delegateDispatcher) {
        this.delegateDispatcher = delegateDispatcher;
    }
    
    
    public boolean dispatch() {
	if(delegateDispatcher != null) {
	    return delegateDispatcher.dispatchEvent(this);
	}
	return false;
    }

    public EventHandle getEventHandle() {
        return eventHandle;
    }

    public void setEventHandle(EventHandle eventHandle) {
        this.eventHandle = eventHandle;
    }
    
}
