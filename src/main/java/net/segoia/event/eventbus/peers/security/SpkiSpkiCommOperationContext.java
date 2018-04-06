package net.segoia.event.eventbus.peers.security;

public class SpkiSpkiCommOperationContext<C extends OperationContext> extends CommOperationContext<C, SpkiPrivateIdentityData, SpkiPublicIdentityManager>{

    public SpkiSpkiCommOperationContext(C opContex, SpkiPrivateIdentityData ourIdentity,
	    SpkiPublicIdentityManager peerIdentity) {
	super(opContex, ourIdentity, peerIdentity);
    }

}
