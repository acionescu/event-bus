package net.segoia.event.eventbus.peers.security;

public class CommOperationContext<C extends OperationContext, O extends PrivateIdentityData<?>, P extends PublicIdentityManager> {
    private C opContex;
    private O ourIdentity;
    private P peerIdentity;
    public CommOperationContext(C opContex, O ourIdentity, P peerIdentity) {
	super();
	this.opContex = opContex;
	this.ourIdentity = ourIdentity;
	this.peerIdentity = peerIdentity;
    }
    public C getOpContex() {
        return opContex;
    }
    public O getOurIdentity() {
        return ourIdentity;
    }
    public P getPeerIdentity() {
        return peerIdentity;
    }
    
    
    
    
}
