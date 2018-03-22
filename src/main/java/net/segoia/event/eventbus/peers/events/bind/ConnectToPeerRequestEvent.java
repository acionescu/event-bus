package net.segoia.event.eventbus.peers.events.bind;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;

@EventType("PEER:CONNECT:REQUEST")
public class ConnectToPeerRequestEvent extends CustomEvent<ConnectToPeerRequest>{

    public ConnectToPeerRequestEvent(ConnectToPeerRequest data) {
	super(ConnectToPeerRequestEvent.class, data);
    }

}
