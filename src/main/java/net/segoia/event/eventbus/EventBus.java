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

public interface EventBus {

    void start();

    void stop();

    /**
     * Posts an {@link Event} to the bus
     * 
     * @param event
     * @return
     */
    InternalEventTracker postEvent(Event event);

    InternalEventTracker postEvent(EventContext eventContext, EventHandle eventHandle);

    void registerListener(EventContextListener listener);

    void registerListener(EventContextListener listener, int priority);

    void removeListener(EventContextListener listener);

    /**
     * Return an {@link EventHandle} to provide more control in the posting of the event
     * 
     * @param event
     * @return
     */
    public EventHandle getHandle(Event event);

    EventBusConfig getConfig();

    EventTypeConfig getConfigForEventType(String eventType);

    EventTypeConfig getConfigForEventType(String eventType, boolean useDefaultIfMissing);

    EventTypeConfig getConfigForEventType(String eventType, EventTypeConfig defaultConfig);

}
