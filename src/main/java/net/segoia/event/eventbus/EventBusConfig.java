package net.segoia.event.eventbus;

import java.util.HashMap;
import java.util.Map;

import net.segoia.event.eventbus.builders.EventBuilder;

/**
 * The {@link EventBus} base configuration
 * @author adi
 *
 * @param <B> - an instance of an {@link EventBuilder} used to create events for this bus
 */
public class EventBusConfig {

    /**
     * Holds the configurations for each event type
     */
    private Map<String, EventTypeConfig> eventsConfig = new HashMap<>();
    /**
     * This will be used in case no other config was specified for a particular event type
     */
    private EventTypeConfig defaultEventConfig=new EventTypeConfig();
    
    private EventRightsManager eventRightsManager;
    
    
    public EventTypeConfig getConfigForEventType(String eventType, boolean useDefaultIfMissing) {
	EventTypeConfig ec = eventsConfig.get(eventType);
	if(ec == null && useDefaultIfMissing) {
	    return defaultEventConfig;
	}
	
	return ec;
    }
    
    public EventTypeConfig getConfigForEventType(String eventType) {
	return eventsConfig.get(eventType);
    }
    
    public EventBusConfig setEventTypeConfig(String eventType, EventTypeConfig config) {
	eventsConfig.put(eventType, config);
	return this;
    }

    /**
     * @return the defaultEventConfig
     */
    public EventTypeConfig getDefaultEventConfig() {
        return defaultEventConfig;
    }

    /**
     * @return the eventsConfig
     */
    public Map<String, EventTypeConfig> getEventsConfig() {
        return eventsConfig;
    }

   

    /**
     * @param eventsConfig the eventsConfig to set
     */
    public void setEventsConfig(Map<String, EventTypeConfig> eventsConfig) {
        this.eventsConfig = eventsConfig;
    }

    /**
     * @param defaultEventConfig the defaultEventConfig to set
     */
    public void setDefaultEventConfig(EventTypeConfig defaultEventConfig) {
        this.defaultEventConfig = defaultEventConfig;
    }

    
    public EventRights getEventRights(EventContext eventContext) {
	if(eventRightsManager == null) {
	    /* return default rights */
	    return defaultEventConfig.getEventRights();
	}
	return eventRightsManager.getEventRights(eventContext);
    }
    
}
