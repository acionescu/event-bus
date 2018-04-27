package net.segoia.event.eventbus.peers;

public interface PeerManagerFactory {
    PeerManager buildPeerManager(PeerContext peerContext);
}
