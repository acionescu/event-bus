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


import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import net.segoia.event.conditions.StrictChannelMatchCondition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.FilteringEventProcessor;
import net.segoia.event.eventbus.PassthroughCustomEventContextListenerFactory;
import net.segoia.event.eventbus.util.EBus;

public class FilteringEventProcessorTest {

    @Test
    public void testRemoveListenerConditionEventContextListener() {
	EBus.initialize();
	
	FilteringEventProcessor eventsProcessor = new FilteringEventProcessor(
		new PassthroughCustomEventContextListenerFactory());
	
	
	Assert.assertEquals(0, eventsProcessor.listenersCount());

	TestEventListener l1 = new TestEventListener();
	
	ArrayList<Object> list = new ArrayList<>();
	list.add(l1);
	list.remove(l1);
	Assert.assertEquals(0, list.size());
	

	StrictChannelMatchCondition ch1Cond = new StrictChannelMatchCondition("ch1");
	eventsProcessor.registerListener(ch1Cond, l1);

	TestEventListener l2 = new TestEventListener();
	
	eventsProcessor.registerListener(ch1Cond, l2);
	
	Assert.assertEquals(2, eventsProcessor.listenersCount());
	
	Assert.assertEquals(2, eventsProcessor.listenersForCondCount(ch1Cond));
	
	String e1type = "test:event:first";
	Event e1 = new Event(e1type);e1.getHeader().setChannel("ch1");
	
	eventsProcessor.processEvent(new EventContext(e1));
	
	Assert.assertEquals(1, l1.getEventsForType(e1type).size());
	
	eventsProcessor.removeListener(ch1Cond, l1);
	
	Assert.assertEquals(1, eventsProcessor.listenersCount());
	
	String e2type = "test:event:second";
	Event e2 = new Event(e2type);e2.getHeader().setChannel("ch1");
	
	eventsProcessor.processEvent(new EventContext(e2));
	
	/* l1 was removed, so it shouldn't get this event */
	Assert.assertNull(l1.getEventsForType(e2type));
	/* l2 should get the event */
	Assert.assertEquals(1, l2.getEventsForType(e2type).size());
    }

}
