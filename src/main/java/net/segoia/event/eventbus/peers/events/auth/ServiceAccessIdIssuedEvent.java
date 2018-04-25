package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("SERVICE:ACCESS_ID:ISSUED")
public class ServiceAccessIdIssuedEvent extends CustomEvent<ServiceAccessIdIssuedData> {

    public ServiceAccessIdIssuedEvent(ServiceAccessIdIssuedData data) {
	super(ServiceAccessIdIssuedEvent.class, data);
    }

}
