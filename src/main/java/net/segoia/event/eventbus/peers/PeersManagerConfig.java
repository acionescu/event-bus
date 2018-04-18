package net.segoia.event.eventbus.peers;

import java.util.Map;

public class PeersManagerConfig {
    private PeerManagerFactory defaultPeerManagerFactory = new DefaultPeerManagerFactory();
    
    private Map<String,PeerManagerFactory> peerManagerFactories;

    public PeerManagerFactory getDefaultPeerManagerFactory() {
	return defaultPeerManagerFactory;
    }

    public void setDefaultPeerManagerFactory(PeerManagerFactory defaultPeerManagerFactory) {
	this.defaultPeerManagerFactory = defaultPeerManagerFactory;
    }

    public Map<String, PeerManagerFactory> getPeerManagerFactories() {
        return peerManagerFactories;
    }

    public void setPeerManagerFactories(Map<String, PeerManagerFactory> peerManagerFactories) {
        this.peerManagerFactories = peerManagerFactories;
    }
    
}
