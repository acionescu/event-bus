package net.segoia.event.eventbus.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventHandle;
import net.segoia.event.eventbus.EventListener;
import net.segoia.event.eventbus.EventTracker;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.builders.DefaultComponentEventBuilder;
import net.segoia.event.eventbus.builders.EventBuilderContext;
import net.segoia.event.eventbus.config.json.EventBusJsonConfig;
import net.segoia.event.eventbus.config.json.EventBusJsonConfigLoader;
import net.segoia.event.eventbus.config.json.EventListenerJsonConfig;

public class EBus {
    private static final String jsonConfigFile = "ebus.json";

    private static FilteringEventBus bus = new FilteringEventBus();

    static {
	/* set if there's any config file */

	File cfgFile = new File(EBus.class.getClassLoader().getResource(jsonConfigFile).getFile());
	if (cfgFile.exists()) {
	    try {
		EventBusJsonConfig ebusJsonConfig = EventBusJsonConfigLoader.load(new FileReader(cfgFile));
		
		String ebusImpl = ebusJsonConfig.getBusClassName();
		
		bus = (FilteringEventBus)Class.forName(ebusImpl).newInstance();
		bus.setConfig(ebusJsonConfig);
		
		Map<String, EventListenerJsonConfig> listeners = ebusJsonConfig.getListeners();
		if(listeners != null) {
		    for(EventListenerJsonConfig lc : listeners.values()) {
			EventListener listenerInstance = lc.getInstance();
			listenerInstance.init();
			
			if(lc.getConditions() == null) {
			    bus.registerListener(listenerInstance, lc.getPriority());
			}
			
		    }
		}
		
		
		
	    } catch (Exception e) {
		System.err.println("Failed to load Ebus config from file " + jsonConfigFile);
		e.printStackTrace();
	    }
	}

    }

    public static EventTracker postEvent(Event event) {
	return bus.postEvent(event);
    }
    
    public static EventHandle getHandle(Event event) {
	return bus.getHandle(event);
    }

    public static FilteringEventBus instance() {
	return bus;
    }

    public static DefaultComponentEventBuilder getComponentEventBuilder(String componentId) {
	return new DefaultComponentEventBuilder(componentId);
    }

    public static boolean isEventAllowed(EventBuilderContext ebc) {

	// TODO: actually check this
	return true;
    }

}
