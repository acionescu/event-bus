package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.peers.comm.NodeCommunicationStrategy;

public abstract class CommManager<O extends PrivateIdentityData<?>,P extends PublicIdentityManager> {
    private NodeCommunicationStrategy txStrategy;
    private NodeCommunicationStrategy rxStrategy;
    private O ourIdentity;
    private P peerIdentity;

    public abstract Event processsOutgoingEvent(EventContext context) throws CommOperationException;

    public abstract Event processIncomingEvent(EventContext context) throws CommOperationException;

    public NodeCommunicationStrategy getTxStrategy() {
	return txStrategy;
    }

    public void setTxStrategy(NodeCommunicationStrategy txStrategy) {
	this.txStrategy = txStrategy;
    }

    public NodeCommunicationStrategy getRxStrategy() {
	return rxStrategy;
    }

    public void setRxStrategy(NodeCommunicationStrategy rxStrategy) {
	this.rxStrategy = rxStrategy;
    }

    public O getOurIdentity() {
        return ourIdentity;
    }

    public void setOurIdentity(O ourIdentity) {
        this.ourIdentity = ourIdentity;
    }

    public P getPeerIdentity() {
        return peerIdentity;
    }

    public void setPeerIdentity(P peerIdentity) {
        this.peerIdentity = peerIdentity;
    }

}
