package net.segoia.event.eventbus;

import net.segoia.event.eventbus.constants.Events;

public class LifecycledEventBus extends FilteringEventBus {
    public static final String START_DISPATCH = "start_dispatch";
    public static final String END_DISPATCH = "end_dispatch";

    public EventTracker postEvent(Event event, EventListener lifecycleEventListener) {
	return super.postEvent(event, lifecycleEventListener);
    }

    /**
     * 
     * @param ec
     * @param el
     * @return
     */
    @Override
    protected EventTracker postEvent(EventContext ec) {
	if (ec.hasLifecycleListener()) {
	    Event startDispatchEvent = Events.builder().system().event().name(START_DISPATCH)
		    .topic(ec.getEvent().getId()).build();
	    ec.sendLifecycleEvent(buildEventContext(startDispatchEvent));
	}
	EventTracker tracker = super.postEvent(ec);
	if (ec.hasLifecycleListener()) {
	    Event endDispatchEvent = Events.builder().system().event().name(END_DISPATCH).topic(ec.getEvent().getId())
		    .build();
	    ec.sendLifecycleEvent(buildEventContext(endDispatchEvent));
	}

	return tracker;
    }

}
