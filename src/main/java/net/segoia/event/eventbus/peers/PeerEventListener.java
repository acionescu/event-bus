package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

public interface PeerEventListener {
    public void onPeerEvent(Event event);
    public void onPeerLeaving();
}
