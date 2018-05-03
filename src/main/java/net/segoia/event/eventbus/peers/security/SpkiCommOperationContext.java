package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.core.PublicIdentityManager;
import net.segoia.event.eventbus.peers.vo.comm.CommOperationDef;

public class SpkiCommOperationContext<D extends CommOperationDef, O extends SpkiPrivateIdentityManager, P extends PublicIdentityManager>
	extends OperationContext<D> {
    private O ourIdentity;
    private P peerIdentity;

    public SpkiCommOperationContext(D opDef, O ourIdentity, P peerIdentity) {
	super(opDef);
	this.ourIdentity = ourIdentity;
	this.peerIdentity = peerIdentity;
    }

    public O getOurIdentity() {
	return ourIdentity;
    }

    public P getPeerIdentity() {
	return peerIdentity;
    }
}
