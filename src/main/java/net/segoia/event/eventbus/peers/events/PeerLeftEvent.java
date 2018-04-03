package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("EBUS:PEER:LEFT")
public class PeerLeftEvent extends CustomEvent<PeerInfo>{

    public PeerLeftEvent(PeerInfo data) {
	super(PeerLeftEvent.class, data);
    }

}
