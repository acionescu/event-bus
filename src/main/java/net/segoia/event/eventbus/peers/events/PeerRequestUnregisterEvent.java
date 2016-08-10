package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:REQUEST:UNREGISTER")
public class PeerRequestUnregisterEvent extends CustomEvent<PeerInfo> {

    public PeerRequestUnregisterEvent(String peerId) {
	super(PeerRequestUnregisterEvent.class);
	this.data = new PeerInfo(peerId);
    }

   

}
