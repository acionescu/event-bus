package net.segoia.event.eventbus.peers;

public class DefaultPeerManagerFactory implements PeerManagerFactory{

    @Override
    public PeerManager buidPeerManager(PeerContext peerContext) {
	return new PeerManager(peerContext);
    }

}
