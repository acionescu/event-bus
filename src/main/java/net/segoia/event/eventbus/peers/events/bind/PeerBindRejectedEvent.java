package net.segoia.event.eventbus.peers.events.bind;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:BIND:REJECTED")
public class PeerBindRejectedEvent extends CustomEvent<PeerBindRejected>{

    public PeerBindRejectedEvent(PeerBindRejected data) {
	super(PeerBindRejectedEvent.class, data);
    }
}