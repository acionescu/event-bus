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

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.peers.EventHandler;

public class FilteringEventBus extends SimpleEventBus {

    public FilteringEventBus() {
	super();
    }

    public FilteringEventBus(EventDispatcher eventDispatcher) {
	super(new BlockingFilteringEventProcessor(eventDispatcher));
    }

    protected FilteringEventProcessor getProcessor() {
	return (FilteringEventProcessor) processor;
    }
    
    public void setEventDispatcher(EventDispatcher eventDispatcher) {
	setProcessor(new BlockingFilteringEventProcessor(eventDispatcher));
    }

    /**
     * Registers a listener for a particular condition with no special priority
     * 
     * @param condition
     * @param listener
     */
    public void registerListener(Condition condition, EventContextListener listener) {
	// getListenerForCondition(condition,-1).registerListener(listener);
	getProcessor().registerListener(condition, listener);
    }

    /**
     * Registers a listener for a particular condition with a given priority
     * 
     * @param condition
     * @param listener
     * @param priority
     *            - this is the priority of this listener among other listeners registered for the same condition
     */
    public void registerListener(Condition condition, EventContextListener listener, int priority) {
	// getListenerForCondition(condition,-1).registerListener(listener, priority);
	getProcessor().registerListener(condition, listener, priority);
    }

    /**
     * Registers a listener for a particular condition with a given priority for the condition listener and the final
     * listener
     * 
     * @param condition
     * @param cPriority
     *            - the priority of the condition filter among other event bus top level listeners
     * @param listener
     * @param lPriority
     *            - final listener priority, among other listeners registered for this condition
     */
    public void registerListener(Condition condition, int cPriority, EventContextListener listener, int lPriority) {
	// getListenerForCondition(condition, cPriority).registerListener(listener, lPriority);
	getProcessor().registerListener(condition, cPriority, listener, lPriority);
    }

    /**
     * Registers a listener for a particular condition with a given priority for the condition listener
     * 
     * @param condition
     * @param cPriority
     * @param listener
     */
    public void registerListener(Condition condition, int cPriority, EventContextListener listener) {
	// getListenerForCondition(condition, cPriority).registerListener(listener);
	getProcessor().registerListener(condition, cPriority, listener);
    }

    public <E extends Event> void addEventHandler(EventHandler<E> handler) {
	// registerListener(getCustomEventListener(handler));
	getProcessor().addEventHandler(handler);
    }

    public <E extends Event> void addEventHandler(EventHandler<E> handler, int priority) {
	// registerListener(getCustomEventListener(handler), priority);
	getProcessor().addEventHandler(handler, priority);
    }

    public <E extends Event> void addEventHandler(Condition condition, EventHandler<E> handler) {
	// registerListener(condition, getCustomEventListener(handler));
	getProcessor().addEventHandler(condition, handler);
    }

    public <E extends Event> void addEventHandler(Class<E> eventClass, EventHandler<E> handler) {
	// addEventHandler(new EventClassMatchCondition(eventClass), handler);
	getProcessor().addEventHandler(eventClass, handler);
    }

    public <E extends Event> void addEventHandler(String eventType, EventHandler<E> handler) {
	// addEventHandler(new StrictEventMatchCondition(eventType), handler);
	getProcessor().addEventHandler(eventType, handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.event.eventbus.SimpleEventBus#clone()
     */
    @Override
    public FilteringEventBus clone() {
	return (FilteringEventBus) super.clone();
    }

}
