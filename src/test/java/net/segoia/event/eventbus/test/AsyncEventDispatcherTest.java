package net.segoia.event.eventbus.test;

import org.junit.Test;

import net.segoia.event.eventbus.AsyncEventDispatcher;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.util.EBus;

public class AsyncEventDispatcherTest {

    @Test
    public void testAsyncWrapperCapacity() {
	FilteringEventBus bus = EBus.instance().clone();
	AsyncEventDispatcher aed = new AsyncEventDispatcher(EBus.instance().getEventDispatcher(), 100);
	bus.setEventDispatcher(aed);
	bus.start();
	
	System.out.println("Starting");
	int i = 0;
	try {

	    for (i = 0; i < 100000; i++) {
		bus.postEvent(new CustomTestEvent("event " + i));
	    }
	} catch (Exception e) {
	    System.out.println("Failed at " + i);
	    e.printStackTrace();
	}
	
	System.out.println("Posted "+i);
    }

}
