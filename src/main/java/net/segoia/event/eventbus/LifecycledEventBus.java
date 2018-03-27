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
package net.segoia.event.eventbus;

import net.segoia.event.eventbus.constants.Events;

public class LifecycledEventBus extends FilteringEventBus {
    public static final String START_DISPATCH = "start_dispatch";
    public static final String END_DISPATCH = "end_dispatch";
    
    public LifecycledEventBus() {
	super();
    }

    public LifecycledEventBus(EventDispatcher eventDispatcher) {
	super(eventDispatcher);
    }

    public InternalEventTracker postEvent(Event event, EventContextListener lifecycleEventListener) {
	return super.postEvent(event, lifecycleEventListener);
    }

    /**
     * 
     * @param ec
     * @param el
     * @return
     */
    @Override
    protected InternalEventTracker postEvent(EventContext ec) {
	if (ec.hasLifecycleListener()) {
	    Event startDispatchEvent = Events.builder().system().event().name(START_DISPATCH)
		    .topic(ec.getEvent().getId()).build();
	    ec.sendLifecycleEvent(buildEventContext(startDispatchEvent));
	}
	InternalEventTracker tracker = super.postEvent(ec);
	if (ec.hasLifecycleListener()) {
	    Event endDispatchEvent = Events.builder().system().event().name(END_DISPATCH).topic(ec.getEvent().getId())
		    .build();
	    ec.sendLifecycleEvent(buildEventContext(endDispatchEvent));
	}

	return tracker;
    }

}
