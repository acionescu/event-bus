package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:AUTH:REQUEST")
public class PeerAuthRequestEvent extends CustomEvent<PeerAuthRequest>{

    public PeerAuthRequestEvent(PeerAuthRequest data) {
	super(PeerAuthRequestEvent.class, data);
	
    }

}