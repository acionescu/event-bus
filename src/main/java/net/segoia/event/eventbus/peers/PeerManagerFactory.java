package net.segoia.event.eventbus.peers;

public interface PeerManagerFactory {
    PeerManager buidPeerManager(PeerContext peerContext);
}
