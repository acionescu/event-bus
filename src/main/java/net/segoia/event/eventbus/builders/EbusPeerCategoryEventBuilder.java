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

public class EbusPeerCategoryEventBuilder extends CategoryRestrictedEventBuilder {

    public EbusPeerCategoryEventBuilder(EventBuilderContext context) {
	super(context, Events.CATEGORY.PEER);
    }

    public EventBuilder newPeer() {
	return super.name(Events.ACTIONS.NEW);

    }

    public EventBuilder peerRemoved() {
	return super.name(Events.ACTIONS.REMOVED);
    }

    public EventBuilder init() {
	return super.name(Events.ACTIONS.INIT);
    }

    public EventBuilder connected() {
	return super.name(Events.ACTIONS.CONNECTED);
    }

    public EventBuilder auth() {
	return super.name(Events.ACTIONS.AUTH);
    }
    
    public EventBuilder authenticated() {
	return super.name(Events.ACTIONS.AUTHENTICATED);
    }
}
