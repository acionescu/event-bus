package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.CommStrategy;

public class CombinedCommProtocolContext extends SpkiCommProtocolContext {

    private SharedSecretIdentityManager sharedIdentityManager;

    public CombinedCommProtocolContext(CommStrategy txStrategy, CommStrategy rxStrategy,
	    SharedSecretIdentityManager sharedIdentityManager, SpkiPrivateIdentityManager ourIdentityManager,
	    SpkiPublicIdentityManager peerIdentityManager) {
	super(txStrategy, rxStrategy,ourIdentityManager,peerIdentityManager);
	
	this.sharedIdentityManager = sharedIdentityManager;
    }

    public SharedSecretIdentityManager getSharedIdentityManager() {
	return sharedIdentityManager;
    }

   

}
