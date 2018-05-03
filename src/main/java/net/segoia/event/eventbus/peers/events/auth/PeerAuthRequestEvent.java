package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.vo.auth.PeerAuthRequest;

@EventType("PEER:AUTH:REQUEST")
public class PeerAuthRequestEvent extends CustomEvent<PeerAuthRequest>{

    public PeerAuthRequestEvent(PeerAuthRequest data) {
	super(PeerAuthRequestEvent.class, data);
	
    }

}
