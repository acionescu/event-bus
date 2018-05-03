package net.segoia.event.eventbus.peers.events.bind;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.vo.bind.DisconnectFromPeerRequest;

@EventType("PEER:DISCONNECT:REQUEST")
public class DisconnectFromPeerRequestEvent extends CustomEvent<DisconnectFromPeerRequest>{

    public DisconnectFromPeerRequestEvent(DisconnectFromPeerRequest data) {
	super(DisconnectFromPeerRequestEvent.class, data);
    }

}
