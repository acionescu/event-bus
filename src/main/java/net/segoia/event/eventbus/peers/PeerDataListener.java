package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.peers.events.PeerDataEvent;

public interface PeerDataListener extends PeerListener{
    public void onPeerData(PeerDataEvent dataEvent);
}
