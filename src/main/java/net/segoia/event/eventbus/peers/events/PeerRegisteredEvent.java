package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:RESPONSE:REGISTERED")
public class PeerRegisteredEvent extends CustomEvent<PeerInfo>{
    
    public PeerRegisteredEvent(String peerId) {
	super(PeerRegisteredEvent.class);
	this.data = new PeerInfo(peerId);
    }

}
