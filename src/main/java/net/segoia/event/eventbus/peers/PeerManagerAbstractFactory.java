package net.segoia.event.eventbus.peers;

import java.util.Map;

public class PeerManagerAbstractFactory implements PeerManagerFactory{
    private PeersManagerConfig config;
    
    

    public PeerManagerAbstractFactory(PeersManagerConfig config) {
	super();
	this.config = config;
    }

    @Override
    public PeerManager buidPeerManager(PeerContext peerContext) {
	String channel = peerContext.getTransceiver().getChannel();
	Map<String, PeerManagerFactory> peerManagerFactories = config.getPeerManagerFactories();
	PeerManagerFactory peerManagerFactory = null;
	if(peerManagerFactories!= null) {
	     peerManagerFactory = peerManagerFactories.get(channel);
	}
	
	if(peerManagerFactory == null) {
	    peerManagerFactory = config.getDefaultPeerManagerFactory();
	}
	return peerManagerFactory.buidPeerManager(peerContext);
    }

    public PeersManagerConfig getConfig() {
        return config;
    }

    public void setConfig(PeersManagerConfig config) {
        this.config = config;
    }
    
    
}
