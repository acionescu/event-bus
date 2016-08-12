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

public abstract class EventDispatcherWrapper implements EventDispatcher{
    protected EventDispatcher nestedDispatcher;

    public EventDispatcherWrapper(EventDispatcher nestedDispatcher) {
	super();
	this.nestedDispatcher = nestedDispatcher;
    }

    @Override
    public void registerListener(EventListener listener) {
	nestedDispatcher.registerListener(listener);
    }

    @Override
    public void registerListener(EventListener listener, int priority) {
	nestedDispatcher.registerListener(listener, priority);
    }

    @Override
    public void removeListener(EventListener listener) {
	nestedDispatcher.registerListener(listener);

    }
    
    @Override
    public void start() {
	nestedDispatcher.start();
	
    }

    @Override
    public void stop() {
	nestedDispatcher.stop();
	
    }

}
