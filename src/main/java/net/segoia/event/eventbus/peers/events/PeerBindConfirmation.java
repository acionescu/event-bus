package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.peers.EventRelay;

public class PeerBindConfirmation {
    private EventRelay relay;

    public PeerBindConfirmation(EventRelay relay) {
	super();
	this.relay = relay;
    }

    /**
     * @return the relay
     */
    public EventRelay getRelay() {
	return relay;
    }

}
