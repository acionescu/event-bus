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

import junit.framework.Assert;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventsRepository;
import net.segoia.event.eventbus.LifecycledEventBus;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.test.test.logger.TestLoggingEventListener;
import net.segoia.util.logging.DefaultLogManager;
import net.segoia.util.logging.Logger;
import net.segoia.util.logging.LoggingLevel;
import net.segoia.util.logging.MasterLogManager;

public class EventTest {

    @Test
    public void testEventLogging() {
	Event e1 = new Event("test:event:e1").addParam("p1", "p1 val").addParam("p2", 56);

	LifecycledEventBus bus = new LifecycledEventBus();

	Logger logger = new DefaultLogManager().getLogger("Test-Logger");
	TestLoggingEventListener loggingListener = new TestLoggingEventListener(logger);
	bus.registerListener(loggingListener);
	
	/* e1 shouldn't be logged */
	bus.postEvent(e1);
	
	Assert.assertFalse(e1.hasTag(TestEventTags.LOGGED));
	
	bus.getConfig().getDefaultEventConfig().setLoggingOn(true);
	Event e2 = new Event("test:event:e2").addParam("p1", "p1 val").addParam("p2", 56);
	bus.postEvent(e2,loggingListener);
	
	Assert.assertTrue(e2.hasTag(TestEventTags.LOGGED));

	logger.setLogLevel(LoggingLevel.ERROR);
	Event e3 = new Event("test:event:e3").addParam("p1", "p1 val").addParam("p2", 56);
	bus.postEvent(e3);
	
	Assert.assertFalse(e3.hasTag(TestEventTags.LOGGED));
	
	bus.getConfig().getDefaultEventConfig().setLoggingLevel(LoggingLevel.ERROR);
	
	Event e4 = new Event("test:event:e4").addParam("p1", "p1 val").addParam("p2", 56);

	bus.postEvent(e4);
	
	Assert.assertTrue(e4.hasTag(TestEventTags.LOGGED));
    }
    
    
    @Test
    public void testLog4jEventLogging() {
	Event e1 = new Event("test:event:e1").addParam("p1", "p1 val").addParam("p2", 56);

	LifecycledEventBus bus = new LifecycledEventBus();

	Logger logger = MasterLogManager.getLogger("Test-Logger");
	TestLoggingEventListener loggingListener = new TestLoggingEventListener(logger);
	bus.registerListener(loggingListener);
	
	/* e1 shouldn't be logged */
	bus.postEvent(e1);
	
	Assert.assertFalse(e1.hasTag(TestEventTags.LOGGED));
	
	bus.getConfig().getDefaultEventConfig().setLoggingOn(true);
	Event e2 = new Event("test:event:e2").addParam("p1", "p1 val").addParam("p2", 56);
	bus.postEvent(e2,loggingListener);
	
	Assert.assertTrue(e2.hasTag(TestEventTags.LOGGED));

	logger.setLogLevel(LoggingLevel.ERROR);
	Event e3 = new Event("test:event:e3").addParam("p1", "p1 val").addParam("p2", 56);
	bus.postEvent(e3);
	
	Assert.assertFalse(e3.hasTag(TestEventTags.LOGGED));
	
	bus.getConfig().getDefaultEventConfig().setLoggingLevel(LoggingLevel.ERROR);
	
	Event e4 = new Event("test:event:e4").addParam("p1", "p1 val").addParam("p2", 56);

	bus.postEvent(e4);
	
	Assert.assertTrue(e4.hasTag(TestEventTags.LOGGED));
    }
    
    
    @Test
    public void testEventBuilder() {
	Event e1 = Events.builder().app().message().name("test1").build();
	
	Event e2 = Events.builder().spawnFrom(e1).app().message().name("test2").build();
	
	Assert.assertEquals(e1.getId(), e2.causeEventId());
	
	Assert.assertTrue(e1.getSpawnedEventsIds().contains(e2.getId()));
    }
    
    @Test
    public void testEventType() {
	/* make sure we use the class first to get loaded */
	new CustomTestEvent("foo bar");
	
	Class eventClass = EventsRepository.getEventClass("TEST:TEST:EVENT");
	Assert.assertEquals(CustomTestEvent.class, eventClass);
    }

}
