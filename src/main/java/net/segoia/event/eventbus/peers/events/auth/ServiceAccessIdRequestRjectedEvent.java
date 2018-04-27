package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("SERVICE:ACCESS_ID:REQUEST_REJECTED")
public class ServiceAccessIdRequestRjectedEvent extends CustomEvent<ServiceAccessIdRequestRejectedReason>{

    public ServiceAccessIdRequestRjectedEvent(ServiceAccessIdRequestRejectedReason data) {
	super(ServiceAccessIdRequestRjectedEvent.class, data);
    }

}
