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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import com.google.gson.JsonObject;

import net.segoia.event.eventbus.util.JsonUtils;

public class DefaultEventsRepository extends EventsRepository {

    public synchronized void checkLoaded() {
	if (!isLoaded) {
	    load();
	}
    }

    public synchronized boolean checkLoaded(String eventType) {
	if (!loadedForEventType.contains(eventType)) {
	    load();
	    loadedForEventType.add(eventType);
	    return false;
	}
	return true;
    }

    public final synchronized void load() {
	try {
	    Field f = ClassLoader.class.getDeclaredField("classes");
	    f.setAccessible(true);

	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    Vector<Class> classes = (Vector<Class>) f.get(classLoader);

	    Collection<Class> clset = Collections.synchronizedCollection(classes);
	    Exception error = null;
	    do {
		try {
		    error = null;
		    for (Class c : clset) {
			processClass(c);
		    }
		} catch (Exception e) {
		    error = e;
		}
	    } while (error != null);
	    isLoaded = true;
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void processClass(Class c) {
	EventType a = (EventType) c.getAnnotation(EventType.class);
	if (a != null) {
	    mapEvent(a.value(), c);
	}
    }

    public Class<Event> getEventClass(String eventType) {
	checkLoaded();
	Class<?> c = eventTypes.get(eventType);
	if (c == null) {
	    if (!checkLoaded(eventType)) {
		c = eventTypes.get(eventType);
		if (c == null) {
		    return Event.class;
		}
	    } else {
		return Event.class;
	    }
	}
	return (Class<Event>) c;
    }

    public String getEventType(Class<?> c) {
	String t = classToEt.get(c);
	if (t == null) {
	    processClass(c);
	    t = classToEt.get(c);
	}
	return t;
    }

    @Override
    public Event fromJson(String json) {
	JsonObject o = JsonUtils.fromJson(json, JsonObject.class);
	String cet = o.get("et").getAsString();

	Class<Event> eclass = getEventClass(cet);

	Event e = JsonUtils.fromJson(json, eclass);

	return e;
    }

    @Override
    public String toJson(Event event) {
	return JsonUtils.toJson(event);
    }

    @Override
    public void mapEvent(String eventType, Class<?> c) {
	eventTypes.put(eventType, c);
	classToEt.put(c, eventType);
    }
}
