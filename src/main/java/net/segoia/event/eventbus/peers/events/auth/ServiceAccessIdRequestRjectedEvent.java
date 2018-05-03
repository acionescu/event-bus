package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.vo.auth.ServiceAccessIdRequestRejectedReason;

@EventType("SERVICE:ACCESS_ID:REQUEST_REJECTED")
public class ServiceAccessIdRequestRjectedEvent extends CustomEvent<ServiceAccessIdRequestRejectedReason>{

    public ServiceAccessIdRequestRjectedEvent(ServiceAccessIdRequestRejectedReason data) {
	super(ServiceAccessIdRequestRjectedEvent.class, data);
    }

}
