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

import net.segoia.event.eventbus.CustomEventContext;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventContextListener;

public class CustomEventListener<E extends Event> implements EventContextListener {
    protected EventHandler<E> eventHandler;

    public CustomEventListener(EventHandler<E> eventHandler) {
	super();
	this.eventHandler = eventHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.event.eventbus.EventListener#onEvent(net.segoia.event.eventbus.EventContext)
     */
    @Override
    public void onEvent(EventContext ec) {
	eventHandler.handleEvent(new CustomEventContext<>(ec));

    }

    
    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.event.eventbus.EventListener#init()
     */
    @Override
    public void init() {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.event.eventbus.EventListener#terminate()
     */
    @Override
    public void terminate() {
	// TODO Auto-generated method stub

    }

}
