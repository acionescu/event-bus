package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:BIND:CONFIRMED")
public class PeerBindConfirmedEvent extends CustomEvent<PeerBindConfirmation> {

    public PeerBindConfirmedEvent(PeerBindConfirmation data) {
	super(PeerBindConfirmedEvent.class);
	this.data = data;
    }

}
