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
	System.out.println(Thread.currentThread().getId() + " -> start load");
	try {
	    Field f = ClassLoader.class.getDeclaredField("classes");
	    f.setAccessible(true);

	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    Vector<Class> classes = (Vector<Class>) f.get(classLoader);
	    System.out.println(classLoader);
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
	System.out.println(Thread.currentThread().getId() + " -> end load");
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
