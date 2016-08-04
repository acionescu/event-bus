package net.segoia.event.eventbus;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

public class EventsRepository {
    private static Map<String, Class<?>> eventTypes;
    
    private static Map<Class<?>,String> classToEt;

    static {
	load();
    }

    public static void load() {
	try {
	    Field f = ClassLoader.class.getDeclaredField("classes");
	    f.setAccessible(true);

	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    Vector<Class> classes = (Vector<Class>) f.get(classLoader);

	    eventTypes = new HashMap<>();
	    classToEt = new HashMap<>();

	    HashSet<Class> clset = new HashSet<>(classes);

	    for (Class c : clset) {
		EventType a = (EventType) c.getAnnotation(EventType.class);
		if (a != null) {
		    eventTypes.put(a.value(), c);
		    classToEt.put(c, a.value());
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    public static Class<Event> getEventClass(String eventType) {
	Class<?> c = eventTypes.get(eventType);
	if (c == null) {
	    return Event.class;
	}
	return (Class<Event>) c;
    }
    
    public static String getEventType(Class<?> c) {
	String t = classToEt.get(c);
	if(t == null) {
	    load();
	    System.out.println("loading..");
	    t = classToEt.get(c);
	}
	return t;
    }
    
    
}
