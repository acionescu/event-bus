package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:AUTH:ACCEPTED")
public class PeerAuthAcceptedEvent extends CustomEvent<PeerAuthAccepted>{

    public PeerAuthAcceptedEvent(PeerAuthAccepted data) {
	super(PeerAuthAcceptedEvent.class, data);
    }

}