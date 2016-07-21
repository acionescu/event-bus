package net.segoia.event.eventbus;

public interface EventBus {

    /**
     * Posts an {@link Event} to the bus
     * @param event
     * @return
     */
    EventTracker postEvent(Event event);
    
    EventTracker postEvent(EventContext eventContext, EventHandle eventHandle);
    
    void registerListener(EventListener listener);
    
    void registerListener(EventListener listener, int priority);
    
    void removeListener(EventListener listener);
    
    /**
     * Return an {@link EventHandle} to provide more control in the posting of the event
     * @param event
     * @return
     */
    public EventHandle getHandle(Event event);
    
    EventBusConfig getConfig();

    EventTypeConfig getConfigForEventType(String eventType);

    EventTypeConfig getConfigForEventType(String eventType, boolean useDefaultIfMissing);

    EventTypeConfig getConfigForEventType(String eventType, EventTypeConfig defaultConfig);
    
}
