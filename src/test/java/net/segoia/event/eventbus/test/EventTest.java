package net.segoia.event.eventbus.test;

import org.junit.Test;

import junit.framework.Assert;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
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
	
	Event e2 = Events.builder().spawnFrom(new EventContext(e1, null)).app().message().name("test2").build();
	
	Assert.assertEquals(e1.getId(), e2.getCauseEventId());
	
	Assert.assertTrue(e1.getSpawnedEventsIds().contains(e2.getId()));
    }

}
