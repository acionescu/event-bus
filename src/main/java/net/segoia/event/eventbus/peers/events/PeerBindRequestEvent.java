package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.PeeringRequest;

@EventType("PEER:REQUEST:BIND")
public class PeerBindRequestEvent extends CustomEvent<PeeringRequest>{

    public PeerBindRequestEvent(PeeringRequest pr) {
	super(PeerBindRequestEvent.class);
	this.data = pr;
    }

}
