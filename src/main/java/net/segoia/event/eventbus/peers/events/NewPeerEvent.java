package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.vo.PeerInfo;

@EventType("EBUS:PEER:NEW")
public class NewPeerEvent extends CustomEvent<PeerInfo>{

    public NewPeerEvent(PeerInfo data) {
	super(NewPeerEvent.class, data);
    }

}
