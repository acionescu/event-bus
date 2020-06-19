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

import java.security.Security;
import java.util.Properties;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import net.segoia.event.eventbus.EBusVM;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventDispatcher;
import net.segoia.event.eventbus.EventHandle;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.InternalEventTracker;
import net.segoia.event.eventbus.builders.DefaultComponentEventBuilder;
import net.segoia.event.eventbus.peers.EventNode;
import net.segoia.event.eventbus.peers.NodeManager;

public class EBus {
    private static EBusVM ebusVm;
    private static EventNode mainNode;

    public static synchronized void initialize(EBusVM ebvm) {
	EBusVM.setInstance(ebvm);
	ebusVm = ebvm;
    }

    public static synchronized void initialize() {
	if (ebusVm == null) {
	    initialize(new DefaultEBusVM());
	}
	
    }

    static {
	initialize();

	if (Security.getProvider("BC") == null) {
	    ebusVm.getLogger().info("Bouncy Castle provider is NOT available. Adding it.");
	    
	    Security.addProvider(new BouncyCastleProvider());
	}
    }

    private static Class getClass(String classname) throws ClassNotFoundException {
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    if(classLoader == null) 
	        classLoader = DefaultEBusVM.class.getClassLoader();
	      return (classLoader.loadClass(classname));
	}
    
    public static NodeManager loadNode(String file) {
	return ebusVm.loadNode(file, true);
    }

    public static NodeManager loadNode(String file, boolean init) {
	return ebusVm.loadNode(file, init);
    }

    public static InternalEventTracker postSystemEvent(Event event) {
	return ebusVm.postSystemEvent(event);
    }

    public static EventHandle getHandle(Event event) {
	return ebusVm.getHandle(event);
    }

    public static FilteringEventBus instance() {
	return ebusVm.getSystemBus();
    }

    public static DefaultComponentEventBuilder getComponentEventBuilder(String componentId) {
	return ebusVm.getComponentEventBuilder(componentId);
    }

    public static EventNode getMainNode() {
	if (mainNode == null) {
	    mainNode = ebusVm.loadNode("main_node.json").getNode();
	}
	return mainNode;
    }

    public static FilteringEventBus buildAsyncFilteringEventBus(int cacheCapacity, int workerThreads,
	    EventDispatcher eventDispatcher) {
	return ebusVm.buildAsyncFilteringEventBus(cacheCapacity, workerThreads, eventDispatcher);
    }

    public static FilteringEventBus buildSingleThreadedAsyncFilteringEventBus(int cacheCapacity,
	    EventDispatcher eventDispatcher) {
	return ebusVm.buildSingleThreadedAsyncFilteringEventBus(cacheCapacity, eventDispatcher);
    }

    public static FilteringEventBus buildSingleThreadedAsyncFilteringEventBus(int cacheCapacity) {
	return ebusVm.buildSingleThreadedAsyncFilteringEventBus(cacheCapacity);
    }

    /**
     * Builds an event bus with the given dispatcher that will function on the main loop
     * 
     * @param eventDispatcher
     * @return
     */
    public static FilteringEventBus buildFilteringEventBusOnMainLoop(EventDispatcher eventDispatcher) {
	return ebusVm.buildFilteringEventBusOnMainLoop(eventDispatcher);
    }

    public static void processAllFromMainLoopAndStop() {
	ebusVm.processAllFromMainLoopAndStop();
    }

    public static void waitToProcessAllOnMainLoop() {
	ebusVm.waitToProcessAllOnMainLoop();
    }

    public static void waitToProcessAllOnMainLoop(int sleep) {
	ebusVm.waitToProcessAllOnMainLoop(sleep);
    }
    
    public static FilteringEventBus getSystemBus() {
	return ebusVm.getSystemBus();
    }
}
