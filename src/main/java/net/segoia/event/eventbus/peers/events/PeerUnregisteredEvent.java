package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:RESPONSE:UNREGISTERED")
public class PeerUnregisteredEvent extends CustomEvent<PeerInfo>{
    
    public PeerUnregisteredEvent(String peerId) {
	super(PeerUnregisteredEvent.class);
	this.data = new PeerInfo(peerId);
    }

   

}
