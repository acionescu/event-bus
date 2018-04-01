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
 * the registered {@link EventContextListener}(s). </br>
 * The event will be passed to each listener in the order of priority. </br>
 * More listeners may have the same priority in which case they will be called in the order of registration
 * 
 * @author adi
 *
 */
public class SimpleEventBus implements EventBus, Cloneable {
    protected SimpleEventProcessor processor;
    private EventBusConfig config = new EventBusConfig();

    public SimpleEventBus(EventDispatcher eventDispatcher) {
	processor = new SimpleEventProcessor(eventDispatcher);
    }

    public SimpleEventBus(SimpleEventProcessor processor) {
	super();
	this.processor = processor;
    }

    public SimpleEventBus() {
	this(new BlockingEventDispatcher());
    }

    public InternalEventTracker postEvent(Event event) {
	EventContext ec = buildEventContext(event);
	return postEvent(ec, getHandle(ec));
    }

    protected InternalEventTracker postEvent(Event event, EventContextListener lifecycleEventListener) {
	EventContext ec = buildEventContext(event, lifecycleEventListener);
	return postEvent(ec, getHandle(ec));
    }

    protected InternalEventTracker postEvent(EventContext ec) {

	prepareEventForPosting(ec.getEvent());
	boolean posted = processor.processEvent(ec);
	return new InternalEventTracker(ec, posted);
    }

    public InternalEventTracker postEventContext(EventContext eventContext) {
	return postEvent(eventContext, getHandle(eventContext));
    }

    protected void prepareEventForPosting(Event event) {
	/* make sure we prevent this event from further modification */
	event.close();
    }

    protected EventContext buildEventContext(Event event) {
	return new EventContext(event);
    }

    protected EventContext buildEventContext(Event event, EventContextListener lifecycleListener) {
	return new EventContext(event, lifecycleListener);
    }

    @Override
    public EventHandle getHandle(Event event) {
	EventContext ec = buildEventContext(event);
	return getHandle(ec);
    }

    protected EventHandle getHandle(EventContext eventContext) {
	EventRights eventRights = config.getEventRights(eventContext);

	return new EventHandle(this, eventContext, eventRights);
    }

    @Override
    public InternalEventTracker postEvent(EventContext eventContext, EventHandle eventHandle) {
	eventContext.setEventHandle(eventHandle);
	if (eventHandle.isAllowed()) {

	    return postEvent(eventContext);
	}
	return new InternalEventTracker(eventContext, false);
    }
    
    protected SimpleEventProcessor getProcessor() {
	return processor;
    }

    protected void setProcessor(SimpleEventProcessor processor) {
	this.processor = processor;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public SimpleEventBus clone() {
	try {
	    return (SimpleEventBus) super.clone();
	} catch (CloneNotSupportedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public void start() {
	processor.start();
    }

    @Override
    public void stop() {
	processor.stop();
    }

    @Override
    public void registerListener(EventContextListener listener) {
	processor.registerListener(listener);
    }

    @Override
    public void registerListener(EventContextListener listener, int priority) {
	processor.registerListener(listener, priority);
    }

    @Override
    public void removeListener(EventContextListener listener) {
	processor.removeListener(listener);
    }

}
