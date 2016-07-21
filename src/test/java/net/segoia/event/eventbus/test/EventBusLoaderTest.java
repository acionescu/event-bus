package net.segoia.event.eventbus.test;

import org.junit.Test;

import junit.framework.Assert;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventBusConfig;
import net.segoia.event.eventbus.EventHandle;
import net.segoia.event.eventbus.EventTypeConfig;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.config.json.EventBusJsonConfig;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.listeners.logging.LoggingEventListener;
import net.segoia.event.eventbus.util.EBus;
import net.segoia.util.logging.Log4jLoggerFactory;
import net.segoia.util.logging.LoggingLevel;

public class EventBusLoaderTest {

    @Test
    public void testLoadFromJson() {
	FilteringEventBus bus = EBus.instance();
	EventBusConfig busConfig = bus.getConfig();
	EventTypeConfig defEventConfig = busConfig.getDefaultEventConfig();

	Assert.assertTrue(defEventConfig.isLoggingOn());
	Assert.assertEquals(LoggingLevel.INFO, defEventConfig.getLoggingLevel());

	EventBusJsonConfig ejsc = (EventBusJsonConfig) busConfig;

	LoggingEventListener lel = (LoggingEventListener) ejsc.getListeners().get("list1").getInstance();

	Assert.assertEquals(lel.getLoggerFactory().getClass(), Log4jLoggerFactory.class);

	EventHandle eh1 = bus.getHandle(Events.builder().scope("BANNED").category("foo").name("bar").build());

	/* events with scope BANNED shouldn't be allowed */
	Assert.assertFalse(eh1.isAllowed());

	/* still, try to post it. It shouldn't be allowed */
	Assert.assertFalse(eh1.post().isPosted());

	EventHandle eh2 = bus.getHandle(Events.builder().scope("OTHER").category("EXECUTING").name("bar").build());

	/* events with category EXECUTING shouldn't be allowed */
	Assert.assertFalse(eh2.isAllowed());

	/* still, try to post it. It shouldn't be allowed */
	Assert.assertFalse(eh2.post().isPosted());
	
	
	EventHandle eh3 = bus.getHandle(Events.builder().system().message().name("hooray").build());
	
	/* this should be allowed */
	Assert.assertTrue(eh3.isAllowed());
	
	Assert.assertTrue(eh3.post().isPosted());
	
	
	Event e4 = Events.builder().scope("BANNED").category("bar").name("foo").build();
	
	/* try to directly post this banned event, it should also be rejected */
	
	Assert.assertFalse(bus.postEvent(e4).isPosted());

    }

}
