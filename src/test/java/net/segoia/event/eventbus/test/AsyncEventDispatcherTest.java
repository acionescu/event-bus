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
package net.segoia.event.eventbus.test;

import org.junit.Test;

import net.segoia.event.eventbus.AsyncEventDispatcher;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.util.EBus;

public class AsyncEventDispatcherTest {

//    @Test
//    public void testAsyncWrapperCapacity() {
//	FilteringEventBus bus = EBus.instance().clone();
//	AsyncEventDispatcher aed = new AsyncEventDispatcher(EBus.instance().getEventDispatcher(), 100);
//	bus.setEventDispatcher(aed);
//	bus.start();
//	
//	System.out.println("Starting");
//	int i = 0;
//	try {
//
//	    for (i = 0; i < 100000; i++) {
//		bus.postEvent(new CustomTestEvent("event " + i));
//	    }
//	} catch (Exception e) {
//	    System.out.println("Failed at " + i);
//	    e.printStackTrace();
//	}
//	
//	System.out.println("Posted "+i);
//    }

}
