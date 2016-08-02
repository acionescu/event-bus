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
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.constants.Events;

public class DefaultEventBuilder extends CustomEventBuilder{
    
    
    
    public DefaultEventBuilder() {
	super();
    }

    public DefaultEventBuilder(EventBuilderContext context) {
	super(context);
    }

    public SystemEventBuilder system() {
	return new SystemEventBuilder(newContext(1));
    }
    
    public AppEventBuilder app() {
	return new AppEventBuilder(newContext(1));
    }
    
    public EbusEventsBuilder ebus() {
	return new EbusEventsBuilder(newContext(1));
    }
    
    public DefaultComponentEventBuilder peer() {
	return new DefaultComponentEventBuilder(Events.SCOPE.PEER);
    }
    
    
    public DefaultEventBuilder spawnFrom( EventContext ec) {
	return spawnFrom(ec.event());
    }
    
    public DefaultEventBuilder spawnFrom ( Event event) {
	EventBuilderContext c = newContext(1);
	c.setCauseEvent(event);
	return new DefaultEventBuilder(c);
    }

    
}
