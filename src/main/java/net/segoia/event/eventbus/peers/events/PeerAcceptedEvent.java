package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("EBUS:PEER:ACCEPTED")
public class PeerAcceptedEvent extends CustomEvent<PeerInfo>{

    public PeerAcceptedEvent(PeerInfo data) {
	super(PeerAcceptedEvent.class, data);
    }

}
