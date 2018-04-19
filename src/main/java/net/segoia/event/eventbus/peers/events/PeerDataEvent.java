package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:DATA:EVENT")
public class PeerDataEvent extends CustomEvent<PeerData>{

    public PeerDataEvent(PeerData data) {
	super(PeerDataEvent.class, data);
    }

}
