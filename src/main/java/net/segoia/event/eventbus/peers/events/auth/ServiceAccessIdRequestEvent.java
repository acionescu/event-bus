package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.vo.auth.ServiceAccessIdRequest;

@EventType("SERVICE:ACCESS_ID:REQUEST")
public class ServiceAccessIdRequestEvent extends CustomEvent<ServiceAccessIdRequest> {

    public ServiceAccessIdRequestEvent(ServiceAccessIdRequest data) {
	super(ServiceAccessIdRequestEvent.class, data);
    }

}
