package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:RESPONSE:BIND")
public class PeerBindResponseEvent extends CustomEvent<PeerBindResponse>{

    public PeerBindResponseEvent(PeerBindResponse pbr) {
	super(PeerBindResponseEvent.class);
	this.data = pbr;
    }

}
