package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.peers.events.RequestRejectReason;

public class MessageAuthRejectedReason extends RequestRejectReason<Object>{

    public MessageAuthRejectedReason() {
	super();
	// TODO Auto-generated constructor stub
    }

    public MessageAuthRejectedReason(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }
    
}
