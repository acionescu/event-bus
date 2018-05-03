package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.vo.comm.NodeCommunicationStrategy;

public class PeerCommContext {
    private int ourIdentityIndex;
    private int peerIdentityIndex;
    private NodeCommunicationStrategy txStrategy;
    private NodeCommunicationStrategy rxStrategy;
    private PeerContext peerContext;

    public PeerCommContext(int ourIdentityIndex, int peerIdentityIndex, NodeCommunicationStrategy txStrategy,
	    NodeCommunicationStrategy rxStrategy, PeerContext peerContext) {
	super();
	this.ourIdentityIndex = ourIdentityIndex;
	this.peerIdentityIndex = peerIdentityIndex;
	this.txStrategy = txStrategy;
	this.rxStrategy = rxStrategy;
	this.peerContext = peerContext;
    }

    public int getOurIdentityIndex() {
	return ourIdentityIndex;
    }

    public int getPeerIdentityIndex() {
	return peerIdentityIndex;
    }

    public NodeCommunicationStrategy getTxStrategy() {
	return txStrategy;
    }

    public NodeCommunicationStrategy getRxStrategy() {
	return rxStrategy;
    }

    public PeerContext getPeerContext() {
	return peerContext;
    }

}
