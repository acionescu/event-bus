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

/**
 * This is a simple {@link EventBus} implementation that will process the posted events sequentially, by delegating to
 * the registered {@link EventListener}(s). </br>
 * The event will be passed to each listener in the order of priority. </br>
 * More listeners may have the same priority in which case they will be called in the order of registration
 * 
 * @author adi
 *
 */
public class SimpleEventBus implements EventBus {
    protected EventDispatcher eventDispatcher = new SimpleEventDispatcher();
    private EventBusConfig config = new EventBusConfig();

    public InternalEventTracker postEvent(Event event) {
	EventContext ec = buildEventContext(event);
	return postEvent(ec,getHandle(ec) );
    }
    
    protected InternalEventTracker postEvent(Event event, EventListener lifecycleEventListener) {
	EventContext ec = buildEventContext(event, lifecycleEventListener);
	return postEvent(ec, getHandle(ec));
    }

    protected InternalEventTracker postEvent(EventContext ec) {
	prepareEventForPosting(ec.getEvent());
	boolean posted = dispatchEvent(ec);
	return new InternalEventTracker(ec, posted);
    }

    protected void prepareEventForPosting(Event event) {
	/* make sure we prevent this event from further modification */
	event.close();
    }

    
    
    protected EventContext buildEventContext(Event event) {
	return new EventContext(event, this);
    }

    protected EventContext buildEventContext(Event event, EventListener lifecycleListener) {
	return new EventContext(event, this, lifecycleListener);
    }

    @Override
    public EventHandle getHandle(Event event) {
	EventContext ec = buildEventContext(event);
	return getHandle(ec);
    }

    protected EventHandle getHandle(EventContext eventContext) {
	EventRights eventRights = config.getEventRights(eventContext);

	return new EventHandle(eventContext, eventRights);
    }

    @Override
    public InternalEventTracker postEvent(EventContext eventContext, EventHandle eventHandle) {
	if (eventHandle.isAllowed()) {
	    return postEvent(eventContext);
	}
	return new InternalEventTracker(eventContext, false);
    }

    protected boolean dispatchEvent(EventContext ec) {
	return eventDispatcher.dispatchEvent(ec);
    }

    public void registerListener(EventListener listener) {
	eventDispatcher.registerListener(listener);
    }

    public void removeListener(EventListener listener) {
	eventDispatcher.removeListener(listener);
    }

    public void registerListener(EventListener listener, int priority) {
	eventDispatcher.registerListener(listener, priority);

    }

    /**
     * @return the config
     */
    @Override
    public EventBusConfig getConfig() {
	return config;
    }

    /**
     * @param config
     *            the config to set
     */
    public void setConfig(EventBusConfig config) {
	this.config = config;
    }

    @Override
    public EventTypeConfig getConfigForEventType(String eventType, EventTypeConfig defaultConfig) {
	EventTypeConfig ec = config.getConfigForEventType(eventType);
	if (ec == null) {
	    return defaultConfig;
	}
	return ec;
    }

    @Override
    public EventTypeConfig getConfigForEventType(String eventType, boolean useDefaultIfMissing) {
	return config.getConfigForEventType(eventType, useDefaultIfMissing);
    }

    @Override
    public EventTypeConfig getConfigForEventType(String eventType) {
	return config.getConfigForEventType(eventType);
    }

}
