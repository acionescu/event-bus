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
