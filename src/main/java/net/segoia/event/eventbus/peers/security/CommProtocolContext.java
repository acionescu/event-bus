package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.NodeCommunicationStrategy;

public class CommProtocolContext {
    private PrivateIdentityData<?> ourIdentity;
    private PublicIdentityManager peerIdentity;
    private NodeCommunicationStrategy txStrategy;
    private NodeCommunicationStrategy rxStrategy;
   

    public CommProtocolContext(PrivateIdentityData<?> ourIdentity, PublicIdentityManager peerIdentity,
	    NodeCommunicationStrategy txStrategy, NodeCommunicationStrategy rxStrategy) {
	super();
	this.ourIdentity = ourIdentity;
	this.peerIdentity = peerIdentity;
	this.txStrategy = txStrategy;
	this.rxStrategy = rxStrategy;
    }

    public PrivateIdentityData<?> getOurIdentity() {
	return ourIdentity;
    }

    public PublicIdentityManager getPeerIdentity() {
	return peerIdentity;
    }

    public NodeCommunicationStrategy getTxStrategy() {
        return txStrategy;
    }

    public NodeCommunicationStrategy getRxStrategy() {
        return rxStrategy;
    }
    
    


}
