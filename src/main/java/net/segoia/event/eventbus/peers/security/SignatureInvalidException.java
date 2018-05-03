package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.core.PublicIdentityManager;

public class SignatureInvalidException extends CommOperationException{
    /**
     * 
     */
    private static final long serialVersionUID = -5598473402900797325L;
    private VerifySignatureOperationContext context;
    private PublicIdentityManager publicIdentity;

   

    public SignatureInvalidException(VerifySignatureOperationContext context, PublicIdentityManager publicIdentity) {
	super();
	this.context = context;
	this.publicIdentity = publicIdentity;
    }



    public VerifySignatureOperationContext getContext() {
        return context;
    }



    public static long getSerialversionuid() {
        return serialVersionUID;
    }



    public PublicIdentityManager getPublicIdentity() {
        return publicIdentity;
    }
    
    
}
