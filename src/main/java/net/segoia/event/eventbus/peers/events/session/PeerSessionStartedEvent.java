package net.segoia.event.eventbus.peers.events.session;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.vo.session.SessionStartedData;

@EventType("PEER:SESSION:STARTED")
public class PeerSessionStartedEvent extends CustomEvent<SessionStartedData> {

    public PeerSessionStartedEvent(SessionStartedData data) {
	super(PeerSessionStartedEvent.class, data);
    }

}
