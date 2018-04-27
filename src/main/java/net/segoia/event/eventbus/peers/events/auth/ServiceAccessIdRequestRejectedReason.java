package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.peers.events.RequestRejectReason;

public class ServiceAccessIdRequestRejectedReason extends RequestRejectReason<Object>{

    public ServiceAccessIdRequestRejectedReason(String message) {
	super(message);
    }

}
