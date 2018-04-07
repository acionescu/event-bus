package net.segoia.event.eventbus.peers;

public interface PeerDataListener extends PeerListener{
    public void onPeerData(byte[] data);
}
