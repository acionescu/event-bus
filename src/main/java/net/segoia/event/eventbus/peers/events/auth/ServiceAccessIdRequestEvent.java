package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("SERVICE:ACCESS_ID:REQUEST")
public class ServiceAccessIdRequestEvent extends CustomEvent<ServiceAccessIdRequest> {

    public ServiceAccessIdRequestEvent(ServiceAccessIdRequest data) {
	super(ServiceAccessIdRequestEvent.class, data);
    }

}
