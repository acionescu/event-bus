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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.TrueCondition;
import net.segoia.event.eventbus.AsyncEventDispatcher;
import net.segoia.event.eventbus.BlockingEventDispatcher;
import net.segoia.event.eventbus.DefaultEventsRepository;
import net.segoia.event.eventbus.DelegatingEventDispatcher;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContextDispatcher;
import net.segoia.event.eventbus.EventContextListener;
import net.segoia.event.eventbus.EventDispatcher;
import net.segoia.event.eventbus.EventHandle;
import net.segoia.event.eventbus.EventsRepository;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.InternalEventTracker;
import net.segoia.event.eventbus.builders.DefaultComponentEventBuilder;
import net.segoia.event.eventbus.config.json.EventBusJsonConfig;
import net.segoia.event.eventbus.config.json.EventBusJsonConfigLoader;
import net.segoia.event.eventbus.config.json.EventListenerJsonConfig;
import net.segoia.event.eventbus.peers.EventBusNodeConfig;
import net.segoia.event.eventbus.peers.EventNode;
import net.segoia.event.eventbus.peers.NodeManager;

public class EBus {
    private static final String jsonConfigFile = "ebus.json";

    private static FilteringEventBus bus = new FilteringEventBus();

    private static EventNode mainNode;
    
    public static boolean debugEnabled;

    /**
     * The main loop event bus dispatcher that will dispatch all events through a single thread
     */
    private static AsyncEventDispatcher mainLoopDispatcher = new AsyncEventDispatcher(new EventContextDispatcher(),
	    1000);

    static {
	EventsRepository.instance = new DefaultEventsRepository();
	
	/* set if there's any config file */

	File cfgFile = new File(EBus.class.getClassLoader().getResource(jsonConfigFile).getFile());
	if (cfgFile.exists()) {
	    
	    try {
		EventBusJsonConfig ebusJsonConfig = EventBusJsonConfigLoader.loadEBusConfig(new FileReader(cfgFile));
		
		debugEnabled = ebusJsonConfig.isDebugEnabled();

		String ebusImpl = ebusJsonConfig.getBusClassName();

		bus = (FilteringEventBus) Class.forName(ebusImpl).newInstance();
		bus.setConfig(ebusJsonConfig);

		AsyncEventDispatcher mainNodeDispatcher = new AsyncEventDispatcher(new EventContextDispatcher(),
			    1000);
		bus.setEventDispatcher(new DelegatingEventDispatcher(new BlockingEventDispatcher(), mainLoopDispatcher));
		//mainNodeDispatcher.start();
		
		
		
		/* create the main event bus node */

		    EventBusNodeConfig nc = new EventBusNodeConfig();
		    /* we will try to get all the events from our peers by default */
		    nc.setDefaultRequestedEvents(new TrueCondition());

		    /* autorelay all events to the peers */
		    nc.setAutoRelayEnabled(true);
		    mainNode = loadNode("main_node.json").getNode();
		    bus.start();
		    
		    /* start main loop dispatcher */
		    mainLoopDispatcher.start();
		
		
		
		Map<String, EventListenerJsonConfig> listeners = ebusJsonConfig.getListeners();
		if (listeners != null) {
		    for (EventListenerJsonConfig lc : listeners.values()) {
			EventContextListener listenerInstance = lc.getInstance();
			listenerInstance.init();

			Condition lcond = lc.getCondition();
			if (lcond == null) {
			    bus.registerListener(listenerInstance, lc.getPriority());
			} else {
			    bus.registerListener(lcond, lc.getCondPriority(), listenerInstance, lc.getPriority());
			}

		    }
		}

	    } catch (Exception e) {
		System.err.println("Failed to load Ebus config from file " + jsonConfigFile);
		e.printStackTrace();
	    }

	   
	}
    }
    public static NodeManager loadNode(String file) {
	return loadNode(file,true);
    }
    
    public static NodeManager loadNode(String file, boolean init) {
	File nodeFile = new File(EBus.class.getClassLoader().getResource(file).getFile());
	
	try {
	    NodeManager nm = EventBusJsonConfigLoader.load(new FileReader(nodeFile), NodeManager.class);
	    if(init) {
		nm.getNode().lazyInit();
	    }
	    return nm;
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
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

    public static FilteringEventBus buildAsyncFilteringEventBus(int cacheCapacity, int workerThreads,
	    EventDispatcher eventDispatcher) {
	AsyncEventDispatcher asyncDispatcher = new AsyncEventDispatcher(eventDispatcher, cacheCapacity, workerThreads);

	FilteringEventBus b = new FilteringEventBus(asyncDispatcher);
	b.start();
	return b;
    }

    public static FilteringEventBus buildSingleThreadedAsyncFilteringEventBus(int cacheCapacity,
	    EventDispatcher eventDispatcher) {
	return buildAsyncFilteringEventBus(cacheCapacity, 1, eventDispatcher);
    }

    public static FilteringEventBus buildSingleThreadedAsyncFilteringEventBus(int cacheCapacity) {
	return buildAsyncFilteringEventBus(cacheCapacity, 1, new BlockingEventDispatcher());
    }

    /**
     * Builds an event bus with the given dispatcher that will function on the main loop
     * 
     * @param eventDispatcher
     * @return
     */
    public static FilteringEventBus buildFilteringEventBusOnMainLoop(EventDispatcher eventDispatcher) {
	FilteringEventBus b = new FilteringEventBus(new DelegatingEventDispatcher(eventDispatcher, mainLoopDispatcher));
	b.start();
	return b;
    }
    
    public static void processAllFromMainLoopAndStop() {
	mainLoopDispatcher.processAllAndStop();
    }
    
    public static void waitToProcessAllOnMainLoop() {
	mainLoopDispatcher.waitToProcessAll();
    }
    
    public static void waitToProcessAllOnMainLoop(int sleep) {
	mainLoopDispatcher.waitToProcessAll(sleep);
    }
}
