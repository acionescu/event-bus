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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.management.DescriptorKey;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.Assert;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventBus;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventListener;
import net.segoia.event.eventbus.SimpleEventBus;

public class EventBusTest {

    @Test
    public void testSimpleListener() {
	EventBus ebus = new SimpleEventBus();

	TestEventListener tl2 = new TestEventListener();
	TestEventListener tl1 = new TestEventListener();

	TestEventListener tl3 = new TestEventListener();
	ebus.registerListener(tl1);

	ebus.registerListener(tl2);

	ebus.registerListener(tl3, 0);

	Event event = new Event("test:event:e1");

	ebus.postEvent(event);

	Assert.assertTrue(tl1.hasReceivedEvent(event));

	Assert.assertTrue(tl2.hasReceivedEvent(event));

	Assert.assertTrue(tl3.hasReceivedEvent(event));

	Assert.assertTrue(tl2.getReceivedTs(event) > tl1.getReceivedTs(event));

	Assert.assertTrue(tl2.getReceivedTs(event) > tl3.getReceivedTs(event));
    }

    @Test
    public void testConditionedEventBus() {
	FilteringEventBus ebus = new FilteringEventBus();

	TestEventListener tl2 = new TestEventListener();
	TestEventListener tl1 = new TestEventListener();
	TestEventListener tl3 = new TestEventListener();
	TestEventListener tl4 = new TestEventListener();

	StrictEventMatchCondition cond1 = new StrictEventMatchCondition("cond1");
	cond1.setEt("test:event:ev");

	StrictEventMatchCondition cond2 = new StrictEventMatchCondition("cond2");
	cond2.setEt("test:event:test2");

	ebus.registerListener(cond1, 5, tl1, 3);
	ebus.registerListener(cond1, tl2);

	ebus.registerListener(cond2, tl3);

	ebus.registerListener(tl4);

	Event e1 = new Event("test:event:ev");
	Event e2 = new Event("test:event:test2");
	Event e3 = new Event("test:event:bla");

	ebus.postEvent(e1);
	ebus.postEvent(e2);
	ebus.postEvent(e3);

	Assert.assertTrue(tl1.hasReceivedEvent(e1));
	Assert.assertTrue(tl2.hasReceivedEvent(e1));
	Assert.assertTrue(tl4.hasReceivedEvent(e1));
	Assert.assertFalse(tl3.hasReceivedEvent(e1));

	Assert.assertTrue(tl4.getReceivedTs(e1) < tl1.getReceivedTs(e1));
	Assert.assertTrue(tl2.getReceivedTs(e1) < tl1.getReceivedTs(e1));
	Assert.assertTrue(tl4.getReceivedTs(e1) < tl2.getReceivedTs(e1));

	Assert.assertTrue(tl3.hasReceivedEvent(e2));
	Assert.assertTrue(tl4.hasReceivedEvent(e2));
	Assert.assertFalse(tl1.hasReceivedEvent(e2));
	Assert.assertFalse(tl2.hasReceivedEvent(e2));

	Assert.assertTrue(tl3.getReceivedTs(e2) < tl4.getReceivedTs(e2));

	Assert.assertTrue(tl4.hasReceivedEvent(e3));
	Assert.assertFalse(tl3.hasReceivedEvent(e3));
	Assert.assertFalse(tl1.hasReceivedEvent(e3));
	Assert.assertFalse(tl2.hasReceivedEvent(e3));

    }

    @Test
    @Ignore // takes too long
    public void testConcurrentPosts() {
	Set<Thread> threads = new HashSet<>();

	EventBus bus = new SimpleEventBus();

	bus.registerListener(new TestEventListener());

	for (int i = 0; i < 3; i++) {
	    ThreadedEventPoster t = new ThreadedEventPoster("test:event:" + i, 10000, bus);
	    threads.add(t);
	    t.start();
	}

	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	for (int k = 0; k < 2; k++) {
	    bus.registerListener(new TestEventListener());
	    try {
		Thread.sleep(10);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	for (Thread t : threads) {
	    try {
		t.join();
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    /**
     * Will post the specified number of events with the specified type to the given bus instance
     * 
     * @author adi
     *
     */
    class ThreadedEventPoster extends Thread {
	private String eventType;
	private int maxPosts;
	private EventBus bus;

	public ThreadedEventPoster(String eventType, int maxPosts, EventBus bus) {
	    super();
	    this.eventType = eventType;
	    this.maxPosts = maxPosts;
	    this.bus = bus;
	}

	public void run() {
	    for (int i = 0; i < maxPosts; i++) {
		boolean posted = bus.postEvent(new Event(eventType)).isPosted();
		if (!posted) {
		    System.out.println("Erron on " + i + " for " + this.getId());
		    return;
		}
//		 System.out.println("Posted event "+i+" by thread "+this.getId());
		Thread.yield();
	    }

	    System.out.println("Thread " + this.getId() + " successfuly finished");
	}
    }

}
