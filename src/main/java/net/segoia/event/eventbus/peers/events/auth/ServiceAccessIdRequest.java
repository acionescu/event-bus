package net.segoia.event.eventbus.peers.events.auth;

import java.util.ArrayList;
import java.util.List;

import net.segoia.event.eventbus.services.EventNodeServiceRef;

public class ServiceAccessIdRequest {
    private List<EventNodeServiceRef> targetServices;

    public ServiceAccessIdRequest(List<EventNodeServiceRef> targetServices) {
	super();
	this.targetServices = targetServices;
    }
    
    

    public ServiceAccessIdRequest(EventNodeServiceRef serviceRef) {
	targetServices = new ArrayList<>();
	targetServices.add(serviceRef);
    }



    public ServiceAccessIdRequest() {
	super();
	// TODO Auto-generated constructor stub
    }



    public List<EventNodeServiceRef> getTargetServices() {
	return targetServices;
    }

    public void setTargetServices(List<EventNodeServiceRef> targetServices) {
	this.targetServices = targetServices;
    }
}
