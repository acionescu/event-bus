package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.comm.NodeCommunicationStrategy;

public class CommProtocolContext {
    private Event event;
    private int ourIdentityIndex;
    private int peerIdentityIndex;
    private NodeCommunicationStrategy commStrategy;
    private PeerContext peerContext;

    public CommProtocolContext(Event event, int ourIdentityIndex, int peerIdentityIndex,
	    NodeCommunicationStrategy commStrategy, PeerContext peerContext) {
	super();
	this.event = event;
	this.ourIdentityIndex = ourIdentityIndex;
	this.peerIdentityIndex = peerIdentityIndex;
	this.commStrategy = commStrategy;
	this.peerContext = peerContext;
    }

    public CommProtocolContext(int ourIdentityIndex, int peerIdentityIndex, PeerContext peerContext) {
	super();
	this.ourIdentityIndex = ourIdentityIndex;
	this.peerIdentityIndex = peerIdentityIndex;
	this.peerContext = peerContext;
    }

    public Event getEvent() {
	return event;
    }

    public int getOurIdentityIndex() {
	return ourIdentityIndex;
    }

    public int getPeerIdentityIndex() {
	return peerIdentityIndex;
    }

    public NodeCommunicationStrategy getCommStrategy() {
	return commStrategy;
    }

    public PeerContext getPeerContext() {
	return peerContext;
    }

    public void setEvent(Event event) {
	this.event = event;
    }

    public void setCommStrategy(NodeCommunicationStrategy commStrategy) {
	this.commStrategy = commStrategy;
    }

}
