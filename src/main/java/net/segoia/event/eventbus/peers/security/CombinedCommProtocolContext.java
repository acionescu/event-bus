package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.CommStrategy;

public class CombinedCommProtocolContext extends CommProtocolContext {

    private SharedSecretIdentityManager sharedIdentityManager;
    private SpkiPrivateIdentityManager ourIdentityManager;
    private SpkiPublicIdentityManager peerIdentityManager;

    public CombinedCommProtocolContext(CommStrategy txStrategy, CommStrategy rxStrategy,
	    SharedSecretIdentityManager sharedIdentityManager, SpkiPrivateIdentityManager ourIdentityManager,
	    SpkiPublicIdentityManager peerIdentityManager) {
	super(txStrategy, rxStrategy);
	this.sharedIdentityManager = sharedIdentityManager;
	this.ourIdentityManager = ourIdentityManager;
	this.peerIdentityManager = peerIdentityManager;
    }

    public SharedSecretIdentityManager getSharedIdentityManager() {
	return sharedIdentityManager;
    }

    public SpkiPrivateIdentityManager getOurIdentityManager() {
	return ourIdentityManager;
    }

    public SpkiPublicIdentityManager getPeerIdentityManager() {
	return peerIdentityManager;
    }

}
