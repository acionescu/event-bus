package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.vo.auth.PeerAuthRejected;

@EventType("PEER:AUTH:REJECTED")
public class PeerAuthRejectedEvent extends CustomEvent<PeerAuthRejected>{

    public PeerAuthRejectedEvent( PeerAuthRejected data) {
	super(PeerAuthRejectedEvent.class, data);
    }

}
