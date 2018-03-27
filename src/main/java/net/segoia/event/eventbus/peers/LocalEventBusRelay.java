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

import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventContextListener;
import net.segoia.event.eventbus.FilteringEventBus;

public class LocalEventBusRelay extends EventRelay implements EventContextListener{
    private FilteringEventBus bus;

    public LocalEventBusRelay(String id, EventNode parentNode, FilteringEventBus bus) {
	super(id, parentNode);
	this.bus = bus;
    }

    @Override
    public void onEvent(EventContext ec) {
	onLocalEvent(ec);
    }


    @Override
    public void init() {
	
    }


    @Override
    public void cleanUp() {
	bus.removeListener(this);
	
    }

    @Override
    protected void start() {
	bus.registerListener(this,999);
	
    }
    
}
