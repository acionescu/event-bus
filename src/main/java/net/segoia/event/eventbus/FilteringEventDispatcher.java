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

public class FilteringEventDispatcher implements EventDispatcher, EventContextListener {
    private Condition condition;
    private EventDispatcher dispatcher;

    public FilteringEventDispatcher(Condition condition) {
	this(condition, new SimpleEventDispatcher());
    }

    public FilteringEventDispatcher(Condition condition, EventDispatcher dispatcher) {
	super();
	this.condition = condition;
	this.dispatcher = dispatcher;
    }

    public void onEvent(EventContext ec) {
	if (condition.test(ec)) {
	    dispatchEvent(ec);
	}

    }

    @Override
    public void init() {
	// TODO Auto-generated method stub

    }

    @Override
    public void terminate() {
	// TODO Auto-generated method stub

    }

    @Override
    public void start() {
	dispatcher.start();
    }

    @Override
    public void stop() {
	dispatcher.stop();
    }

    @Override
    public boolean dispatchEvent(EventContext ec) {
	return dispatcher.dispatchEvent(ec);
    }

    @Override
    public void registerListener(EventContextListener listener) {
	dispatcher.registerListener(listener);
    }

    @Override
    public void registerListener(EventContextListener listener, int priority) {
	dispatcher.registerListener(listener, priority);
    }

    @Override
    public void removeListener(EventContextListener listener) {
	dispatcher.removeListener(listener);
    }

}
