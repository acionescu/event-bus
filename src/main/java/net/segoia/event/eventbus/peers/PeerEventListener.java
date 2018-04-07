package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

public interface PeerEventListener extends PeerListener{
    public void onPeerEvent(Event event);
    
}
