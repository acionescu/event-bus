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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class EventsRepository {
    private static Map<String, Class<?>> eventTypes = new HashMap<>();

    private static Map<Class<?>, String> classToEt = new HashMap<>();

    private static boolean isLoaded;

    public static synchronized void checkLoaded() {
	if (!isLoaded) {
	    load();
	}
    }

    public static final synchronized void load() {
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

    public static void processClass(Class c) {
	EventType a = (EventType) c.getAnnotation(EventType.class);
	if (a != null) {
	    eventTypes.put(a.value(), c);
	    classToEt.put(c, a.value());
	}
    }

    public static Class<Event> getEventClass(String eventType) {
	checkLoaded();
	Class<?> c = eventTypes.get(eventType);
	if (c == null) {
	    return Event.class;
	}
	return (Class<Event>) c;
    }

    public static String getEventType(Class<?> c) {
	String t = classToEt.get(c);
	if (t == null) {
	    processClass(c);
	    t = classToEt.get(c);
	}
	return t;
    }

}
