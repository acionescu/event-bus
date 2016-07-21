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
    public EventTracker postEvent(Event event) {
	return eventBus.postEvent(event);
    }
}
