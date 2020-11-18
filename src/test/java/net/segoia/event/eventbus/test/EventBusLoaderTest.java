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
import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.eventbus.EBusVM;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventBusConfig;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventHandle;
import net.segoia.event.eventbus.EventTypeConfig;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.config.json.EventBusJsonConfig;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.listeners.logging.LoggingEventListener;
import net.segoia.util.logging.Log4jLoggerFactory;
import net.segoia.util.logging.LoggingLevel;

public class EventBusLoaderTest {

    @Test
    public void testLoadFromJson() {
	FilteringEventBus bus = EBusVM.getInstance().getSystemBus();
	EventBusConfig busConfig = bus.getConfig();
	EventTypeConfig defEventConfig = busConfig.getDefaultEventConfig();

	Assert.assertTrue(defEventConfig.isLoggingOn());
	Assert.assertEquals(LoggingLevel.INFO.toString(), defEventConfig.getLoggingLevel());

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

    @Test
    public void testCondListenerLoad() {
	
	EventHandle eh1 = Events.builder().app().user().login().getHandle();
	
	
	
	EventHandle eh2 = Events.builder().app().user().logout().getHandle();
	
	
	
	final Event trackedEvent = eh1.event();
	
	String echoListenerKey ="userLoginEchoKey";
	
	
	TestEventListener tl = new TestEventListener();
	
	tl.setTestCondition(new Condition("tc") {

	    @Override
	    public boolean test(EventContext input) {
		Event ce = input.event();
		if(!ce.causeEventId().equals(trackedEvent.getId())) {
		    return false;
		}
		if(!ce.getEt().equals("TEST:TEST:ECHO")) {
		    return false;
		}
		if(!ce.getTopic().equals(echoListenerKey)) {
		    return false;
		}
		
		return true;
	    }
	    
	    
	});
	
	EBusVM.getInstance().getSystemBus().registerListener(new StrictEventMatchCondition("tlc","TEST:TEST:ECHO"), tl);
	
	
	eh1.post();
	eh2.post();
	
	try {
	    Thread.sleep(50);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	/* check that our listener actually received an echo from the listener registered for APP:USER:LOGIN event */
	Assert.assertTrue(tl.isConditionSatisfied());
	
	/* check that no echo was received for the other event type */
	Assert.assertEquals(1,tl.getEventsForType("TEST:TEST:ECHO").size());
	
	
    }
    
}
