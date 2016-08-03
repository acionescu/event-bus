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

import net.segoia.event.eventbus.constants.Events;

public class DefaultComponentCategory extends EventBuilder {

    public DefaultComponentCategory(EventBuilderContext context, String category) {
	super(context);
	category(category);
    }

    public EventBuilder initializing() {
	return super.name(Events.ACTIONS.INITIALIZING);
    }

    public EventBuilder initialized() {
	return super.name(Events.ACTIONS.INITIALIZED);
    }

    public EventBuilder terminating() {
	return super.name(Events.ACTIONS.TERMINATING);
    }

    public EventBuilder terminated() {
	return super.name(Events.ACTIONS.TERMINATED);
    }
    
    public EventBuilder executing() {
	return super.name(Events.ACTIONS.EXECUTING);
    }
    
    public EventBuilder executed() {
	return super.name(Events.ACTIONS.EXECUTED);
    }
    
    public EventBuilder changed() {
	return super.name(Events.ACTIONS.CHANGED);
    }
    
    public EventBuilder updated() {
	return super.name(Events.ACTIONS.UPDATED);
    }
    
    public EventBuilder auth() {
	return super.name(Events.ACTIONS.AUTH);
    }
    
    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.builders.EventBuilder#name(java.lang.String)
     */
    @Override
    public EventBuilder name(String name) {
	return super.name(name);
    }
    
    
}
