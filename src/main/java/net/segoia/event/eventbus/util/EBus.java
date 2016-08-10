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
package net.segoia.event.eventbus.util;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.TrueCondition;
import net.segoia.event.eventbus.AsyncEventDispatcher;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventDispatcher;
import net.segoia.event.eventbus.EventHandle;
import net.segoia.event.eventbus.EventListener;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.InternalEventTracker;
import net.segoia.event.eventbus.SimpleEventDispatcher;
import net.segoia.event.eventbus.builders.DefaultComponentEventBuilder;
import net.segoia.event.eventbus.config.json.EventBusJsonConfig;
import net.segoia.event.eventbus.config.json.EventBusJsonConfigLoader;
import net.segoia.event.eventbus.config.json.EventListenerJsonConfig;
import net.segoia.event.eventbus.peers.EventBusNodeConfig;
import net.segoia.event.eventbus.peers.EventNode;
import net.segoia.event.eventbus.peers.LocalEventBusNode;

public class EBus {
    private static final String jsonConfigFile = "ebus.json";

    private static FilteringEventBus bus = new FilteringEventBus();
    
    private static LocalEventBusNode mainNode;

    static {
	/* set if there's any config file */

	File cfgFile = new File(EBus.class.getClassLoader().getResource(jsonConfigFile).getFile());
	if (cfgFile.exists()) {
	    try {
		EventBusJsonConfig ebusJsonConfig = EventBusJsonConfigLoader.load(new FileReader(cfgFile));
		
		String ebusImpl = ebusJsonConfig.getBusClassName();
		
		bus = (FilteringEventBus)Class.forName(ebusImpl).newInstance();
		bus.setConfig(ebusJsonConfig);
		
		Map<String, EventListenerJsonConfig> listeners = ebusJsonConfig.getListeners();
		if(listeners != null) {
		    for(EventListenerJsonConfig lc : listeners.values()) {
			EventListener listenerInstance = lc.getInstance();
			listenerInstance.init();
			
			Condition lcond = lc.getCondition();
			if(lcond == null) {
			    bus.registerListener(listenerInstance, lc.getPriority());
			}
			else {
			    bus.registerListener(lcond, lc.getCondPriority(), listenerInstance, lc.getPriority());
			}
			
		    }
		}
		
		
		
	    } catch (Exception e) {
		System.err.println("Failed to load Ebus config from file " + jsonConfigFile);
		e.printStackTrace();
	    }
	    
	    /* create the main event bus node */
	    
	    EventBusNodeConfig nc = new EventBusNodeConfig();
	    /* we will try to get all the events from our peers by default */
	    nc.setDefaultRequestedEvents(new TrueCondition());
	    
	    /* autorelay all events to the peers */
	    nc.setAutoRelayEnabled(true);
	    mainNode = new LocalEventBusNode(bus, nc);
	    
	}

    }

    public static InternalEventTracker postEvent(Event event) {
	return bus.postEvent(event);
    }
    
    public static EventHandle getHandle(Event event) {
	return bus.getHandle(event);
    }

    public static FilteringEventBus instance() {
	return bus;
    }

    public static DefaultComponentEventBuilder getComponentEventBuilder(String componentId) {
	return new DefaultComponentEventBuilder(componentId);
    }
    
    
    public static EventNode getMainNode() {
	return mainNode;
    }
    
    
    public static FilteringEventBus buildAsyncFilteringEventBus(int cacheCapacity, int workerThreads, EventDispatcher eventDispatcher) {
	AsyncEventDispatcher asyncDispatcher = new AsyncEventDispatcher(eventDispatcher, cacheCapacity,workerThreads);
	
	FilteringEventBus b = new FilteringEventBus(asyncDispatcher);
	b.start();
	return b;
    }
    
    public static FilteringEventBus buildSingleThreadedAsyncFilteringEventBus(int cacheCapacity, EventDispatcher eventDispatcher) {
	return buildAsyncFilteringEventBus(cacheCapacity, 1, eventDispatcher);
    }
    public static FilteringEventBus buildSingleThreadedAsyncFilteringEventBus(int cacheCapacity) {
	return buildAsyncFilteringEventBus(cacheCapacity, 1, new SimpleEventDispatcher());
    }
    
    
}
