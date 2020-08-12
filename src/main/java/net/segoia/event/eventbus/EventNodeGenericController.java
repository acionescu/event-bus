package net.segoia.event.eventbus;

import java.util.HashMap;
import java.util.Map;

public abstract class EventNodeGenericController<C> {
    protected C controllerContext;

    protected FilteringEventProcessor eventsProcessor = new FilteringEventProcessor(
	    new PassthroughCustomEventContextListenerFactory());

    protected FilteringEventProcessor localEventsProcessor = new FilteringEventProcessor(
	    new PassthroughCustomEventContextListenerFactory());
    
    private Map<String, EventNodeGenericController<?>> nestedControllers=new HashMap<>();


    protected abstract void registerEventHandlers();

    public void init(C controllerContext) {
	this.controllerContext = controllerContext;
	registerEventHandlers();
    }

    public void processEvent(EventContext ec) {
	eventsProcessor.processEvent(ec);

	for (EventNodeGenericController nac : nestedControllers.values()) {
	    nac.processEvent(ec);
	}
    }

    public void processLocalEvent(EventContext ec) {
	localEventsProcessor.processEvent(ec);

	for (EventNodeGenericController nac : nestedControllers.values()) {
	    nac.processLocalEvent(ec);
	}
    }
    
    public void addNestedController(String id, EventNodeGenericController<?> controller) {
   	nestedControllers.put(id, controller);
       }
       
       public void removeNestedController(String id) {
   	nestedControllers.remove(id);
       }
}
