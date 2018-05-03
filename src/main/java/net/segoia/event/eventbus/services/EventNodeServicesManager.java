package net.segoia.event.eventbus.services;

import java.util.HashMap;
import java.util.Map;

import net.segoia.event.eventbus.vo.services.EventNodePublicServiceDesc;
import net.segoia.event.eventbus.vo.services.EventNodeServiceRef;

public class EventNodeServicesManager {
    private Map<EventNodeServiceRef, EventNodeServiceContext> services = new HashMap<>();

    public void addService(EventNodeServiceContext serviceContext) {
	EventNodePublicServiceDesc serviceDesc = serviceContext.getServiceDef().getServiceDesc();
	services.put(new EventNodeServiceRef(serviceDesc.getServiceId(), serviceDesc.getVersion()), serviceContext);
    }
    
    public void removeService(EventNodeServiceRef serviceRef) {
	services.remove(serviceRef);
    }
    
    public EventNodeServiceContext getService(EventNodeServiceRef serviceRef) {
	return services.get(serviceRef);
    }
}
