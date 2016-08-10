package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("EBUS:PEER:LEAVING")
public class PeerLeavingEvent extends CustomEvent<PeerInfo>{

    public PeerLeavingEvent(String peerId) {
	super(PeerLeavingEvent.class);
	this.data = new PeerInfo(peerId);
    }

}
