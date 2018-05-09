package net.segoia.event.eventbus.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.TrueCondition;
import net.segoia.event.eventbus.AsyncEventDispatcher;
import net.segoia.event.eventbus.BlockingEventDispatcher;
import net.segoia.event.eventbus.BlockingFilteringEventDispatcher;
import net.segoia.event.eventbus.DefaultEventsRepository;
import net.segoia.event.eventbus.DelegatingEventDispatcher;
import net.segoia.event.eventbus.EBusVM;
import net.segoia.event.eventbus.EventContextDispatcher;
import net.segoia.event.eventbus.EventContextListener;
import net.segoia.event.eventbus.EventDispatcher;
import net.segoia.event.eventbus.EventsRepository;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.FilteringEventDispatcher;
import net.segoia.event.eventbus.config.json.EventBusJsonConfig;
import net.segoia.event.eventbus.config.json.EventBusJsonConfigLoader;
import net.segoia.event.eventbus.config.json.EventListenerJsonConfig;
import net.segoia.event.eventbus.peers.EventBusNodeConfig;
import net.segoia.event.eventbus.peers.NodeManager;

public class DefaultEBusVM extends EBusVM {
    /**
     * The main loop event bus dispatcher that will dispatch all events through a single thread
     */
    private static AsyncEventDispatcher mainLoopDispatcher = new AsyncEventDispatcher(new EventContextDispatcher(),
	    1000);

    public DefaultEBusVM() {
	super();
	init();
    }

    protected void init() {
	EventsRepository.instance = new DefaultEventsRepository();
    }

    @Override
    public FilteringEventBus loadBusFromJsonFile(String filePath) {

	/* set if there's any config file */

	File cfgFile = new File(EBus.class.getClassLoader().getResource(filePath).getFile());
	if (cfgFile.exists()) {

	    try {
		EventBusJsonConfig ebusJsonConfig = EventBusJsonConfigLoader.loadEBusConfig(new FileReader(cfgFile));

		debugEnabled = ebusJsonConfig.isDebugEnabled();

		String ebusImpl = ebusJsonConfig.getBusClassName();

		FilteringEventBus bus = (FilteringEventBus) Class.forName(ebusImpl).newInstance();
		bus.setConfig(ebusJsonConfig);

		AsyncEventDispatcher mainNodeDispatcher = new AsyncEventDispatcher(new EventContextDispatcher(), 1000);
		bus.setEventDispatcher(
			new DelegatingEventDispatcher(new BlockingEventDispatcher(), mainLoopDispatcher));
		// mainNodeDispatcher.start();

		/* create the main event bus node */

		EventBusNodeConfig nc = new EventBusNodeConfig();
		/* we will try to get all the events from our peers by default */
		nc.setDefaultRequestedEvents(new TrueCondition());

		/* autorelay all events to the peers */
		nc.setAutoRelayEnabled(true);

		bus.start();

		/* start main loop dispatcher */
		mainLoopDispatcher.start();

		Map<String, EventListenerJsonConfig> listeners = ebusJsonConfig.getListeners();
		if (listeners != null) {
		    for (EventListenerJsonConfig lc : listeners.values()) {
			EventContextListener listenerInstance = lc.getInstance();
			listenerInstance.init();

			Condition lcond = lc.getCondition();
			if (lcond == null) {
			    bus.registerListener(listenerInstance, lc.getPriority());
			} else {
			    bus.registerListener(lcond, lc.getCondPriority(), listenerInstance, lc.getPriority());
			}

		    }
		}
		return bus;

	    } catch (Exception e) {
		System.err.println("Failed to load Ebus config from file " + filePath);
		e.printStackTrace();
	    }

	}
	return null;
    }

    @Override
    public FilteringEventBus buildFilteringEventBusOnMainLoop(EventDispatcher eventDispatcher) {
	FilteringEventBus b = new FilteringEventBus(new DelegatingEventDispatcher(eventDispatcher, mainLoopDispatcher));
	b.start();
	return b;
    }

    @Override
    public void processAllFromMainLoopAndStop() {
	mainLoopDispatcher.processAllAndStop();
    }

    @Override
    public void waitToProcessAllOnMainLoop() {
	mainLoopDispatcher.waitToProcessAll();
    }

    @Override
    public void waitToProcessAllOnMainLoop(int sleep) {
	mainLoopDispatcher.waitToProcessAll(sleep);
    }

    @Override
    public FilteringEventBus buildAsyncFilteringEventBus(int cacheCapacity, int workerThreads,
	    EventDispatcher eventDispatcher) {
	AsyncEventDispatcher asyncDispatcher = new AsyncEventDispatcher(eventDispatcher, cacheCapacity, workerThreads);

	FilteringEventBus b = new FilteringEventBus(asyncDispatcher);
	b.start();
	return b;
    }

    @Override
    public FilteringEventBus buildSingleThreadedAsyncFilteringEventBus(int cacheCapacity) {
	return buildAsyncFilteringEventBus(cacheCapacity, 1, new BlockingEventDispatcher());
    }

    @Override
    public NodeManager loadNode(String file, boolean init) {
	File nodeFile = new File(EBus.class.getClassLoader().getResource(file).getFile());

	try {
	    NodeManager nm = EventBusJsonConfigLoader.load(new FileReader(nodeFile), NodeManager.class);
	    if (init) {
		nm.getNode().lazyInit();
	    }
	    return nm;
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public EventDispatcher buildBlockingEventDispatcher() {
	return new BlockingEventDispatcher();
    }
}