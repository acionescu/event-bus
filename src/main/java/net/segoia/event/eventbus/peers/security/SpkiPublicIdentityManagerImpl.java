package net.segoia.event.eventbus.peers.security;

import java.security.PublicKey;

import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentity;

public class SpkiPublicIdentityManagerImpl implements SpkiPublicIdentityManager{
    private PublicKey publicKey;
    private SpkiNodeIdentity identity;
    
    public SpkiPublicIdentityManagerImpl(SpkiNodeIdentity identity) {
	this.identity = identity;
    }

    @Override
    public byte[] ecryptPublic(EncryptOperationContext context) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean verifySignature(VerifySignatureOperationContext context) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public String getType() {
	return identity.getType().getType();
    }

}
