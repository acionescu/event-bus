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

public class GenericEvent extends Event{

    public GenericEvent() {
	super();
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(Class<?> clazz) {
	super(clazz);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String scope, String category, String name, Event cause, String topic) {
	super(scope, category, name, cause, topic);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String scope, String category, String name, Event cause) {
	super(scope, category, name, cause);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String scope, String category, String name, String topic) {
	super(scope, category, name, topic);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String scope, String category, String name) {
	super(scope, category, name);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String et, String topic) {
	super(et, topic);
	// TODO Auto-generated constructor stub
    }

    public GenericEvent(String et) {
	super(et);
	// TODO Auto-generated constructor stub
    }

}
