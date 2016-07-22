package net.segoia.event.eventbus;

public class EventHandle {
    private EventContext eventContext;
    private EventRights eventRights;

    public EventHandle(EventContext eventContext, EventRights eventRights) {
	super();
	this.eventContext = eventContext;
	this.eventRights = eventRights;
    }

    public boolean isAllowed() {
	return eventRights.isAllowed();
    }

    public EventTracker post() {
	return eventContext.bus().postEvent(eventContext, this);
    }

    /**
     * @return the eventRights
     */
    public EventRights getEventRights() {
	return eventRights;
    }

    public Event getEvent() {
	return eventContext.getEvent();
    }

    public Event event() {
	return eventContext.event();
    }
    
    public EventHandle addParam(String key, Object value) {
	eventContext.event().addParam(key, value);
	return this;
    }
}
