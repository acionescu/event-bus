package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.CommStrategy;

public class SpkiCommProtocolContext extends CommProtocolContext {
    private SpkiPrivateIdentityManager ourIdentity;
    private SpkiPublicIdentityManager peerIdentity;

    public SpkiCommProtocolContext(CommStrategy txStrategy, CommStrategy rxStrategy,
	    SpkiPrivateIdentityManager ourIdentity, SpkiPublicIdentityManager peerIdentity) {
	super(txStrategy, rxStrategy);
	this.ourIdentity = ourIdentity;
	this.peerIdentity = peerIdentity;
    }

    public SpkiPrivateIdentityManager getOurIdentity() {
	return ourIdentity;
    }

    public SpkiPublicIdentityManager getPeerIdentity() {
	return peerIdentity;
    }
}
