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

public class SystemEventBuilder extends ScopeRestrictedEventBuilder{

    public SystemEventBuilder(EventBuilderContext context) {
	super(context,Events.SCOPE.SYSTEM);
    }
    
    
    public CategoryRestrictedEventBuilder message() {
	return category(Events.CATEGORY.MESSAGE);
    }
    
    public CategoryRestrictedEventBuilder error() {
	return category(Events.CATEGORY.ERROR);
    }

    public CategoryRestrictedEventBuilder alert() {
	return category(Events.CATEGORY.ALERT);
    }
    
    public CategoryRestrictedEventBuilder event() {
	return category(Events.CATEGORY.EVENT);
    }
    
}
