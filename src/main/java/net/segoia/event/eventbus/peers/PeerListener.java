package net.segoia.event.eventbus.peers;

public interface PeerListener {
    public void onPeerLeaving(PeerLeavingReason reason);
}
