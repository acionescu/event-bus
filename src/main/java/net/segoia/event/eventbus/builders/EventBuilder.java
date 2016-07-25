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
package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventHandle;
import net.segoia.event.eventbus.util.EBus;

public abstract class EventBuilder {

    protected EventBuilderContext context;

    public EventBuilder(EventBuilderContext context) {
	super();
	this.context = context;
    }

    protected EventBuilder scope(String scope) {
	context.setScope(scope);
	return this;
    }

    protected EventBuilder category(String category) {
	context.setCategory(category);
	return this;
    }

    protected EventBuilder name(String name) {
	context.setName(name);
	return this;
    }

    public EventBuilder topic(String topic) {
	context.setTopic(topic);
	return this;
    }
    
    protected EventBuilderContext newContext(int thresholdDepth) {
	/* this prevents overwriting the template context, and also unnecessary duplication */
	if(context.getDepth()>=thresholdDepth) {
	    return context;
	}
	return context.copy();
    }

    public Event build() {

	Event causeEvent = context.getCauseEvent();
	String topic = context.getTopic();

	if (causeEvent != null) {
	    return build(context.getScope(), context.getCategory(), context.getName(), causeEvent, topic);
	}
	return build(context.getScope(), context.getCategory(), context.getName(), topic);
    }

    private Event build(String scope, String category, String name) {

	return build(scope, category, name, null, null);
    }

    private Event build(String scope, String category, String name, Event cause) {
	return build(scope, category, name, cause, null);
    }

    private Event build(String scope, String category, String name, String topic) {
	return build(scope, category, name, null, topic);
    }

    private Event build(String scope, String category, String name, Event cause, String topic) {
	return new Event(scope, category, name, cause, topic);
    }
    
    public EventHandle getHandle() {
	return EBus.getHandle(build());
    }
}
