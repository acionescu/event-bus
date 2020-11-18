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
package net.segoia.event.eventbus.peers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.BlockingEventDispatcher;
import net.segoia.event.eventbus.EBusVM;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventDispatcher;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.peers.security.DefaultEventNodeSecurityManager;
import net.segoia.event.eventbus.peers.security.DefaultIdentitiesManager;
import net.segoia.event.eventbus.peers.security.EventNodeSecurityConfig;
import net.segoia.event.eventbus.peers.security.EventNodeSecurityManager;

public class DefaultEventNode extends EventNode {

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void nodeInit() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void nodeConfig() {
	EventNodeSecurityConfig securityConfig = config.getSecurityConfig();
	if (securityConfig.getIdentitiesManager() == null) {
	    securityConfig.setIdentitiesManager(new DefaultIdentitiesManager());
	}
	super.nodeConfig();
    }

    @Override
    protected void setRequestedEventsCondition() {
	// TODO Auto-generated method stub

    }

    @Override
    public void cleanUp() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void onTerminate() {
	// TODO Auto-generated method stub

    }

    @Override
    protected FilteringEventBus buildInternalBus() {
	return EBusVM.getInstance().buildFilteringEventBusOnMainLoop(new EventNodeDispatcher());
    }

    @Override
    protected FilteringEventBus spawnAdditionalBus(EventDispatcher eventDispatcher) {
	return EBusVM.getInstance().buildFilteringEventBusOnMainLoop(eventDispatcher);
    }

    @Override
    protected EventNodeSecurityManager buildSecurityManager(EventNodeSecurityConfig securityConfig) {
	return new DefaultEventNodeSecurityManager(config);
    }

    class EventNodeDispatcher extends BlockingEventDispatcher {

	@Override
	public boolean dispatchEvent(EventContext ec) {
	    stats.onEvent(ec);
	    boolean forUs = handleRemoteEvent(ec);
	    if (forUs || config.isGod()) {
		Event event = ec.getEvent();
		/* if no from address, then this comes from us */
		if (event.from() == null) {
		    event.addRelay(getId());
		}
		/* dispatch this further only if this event is meant for us */
		return super.dispatchEvent(ec);
	    }
	    return false;
	}

    }

    @Override
    protected FilteringEventBus spawnEventBus(Condition cond) {
	return spawnEventBus(cond, new BlockingEventDispatcher());
    }

    @Override
    public void scheduleEvent(final Event event, long delay) {
	if (event == null) {
	    throw new IllegalArgumentException("Can't schedule a null event");
	}

	executorService.schedule(() -> {
	    try {
		postInternally(event);
		postToExtraBusses(event);
	    } catch (Throwable t) {
		getConfig().getLogger().error("Failed to schedule event " + event.getEt(), t);
	    }
	}, delay, TimeUnit.MILLISECONDS);

    }

}
