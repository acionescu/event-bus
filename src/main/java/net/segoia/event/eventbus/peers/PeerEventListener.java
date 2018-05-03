package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.core.PeerListener;

public interface PeerEventListener extends PeerListener{
    public void onPeerEvent(Event event);
    
}
