package net.segoia.event.eventbus.peers.security;

import java.security.PublicKey;

import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentity;
import net.segoia.util.crypto.CryptoUtil;

public class SpkiPublicIdentityManagerImpl implements SpkiPublicIdentityManager {
    private PublicKey publicKey;
    private SpkiNodeIdentity identity;

    public SpkiPublicIdentityManagerImpl(SpkiNodeIdentity identity) {
	this.identity = identity;
	try {
	    publicKey = CryptoUtil.getPublicKeyFromBase64EncodedString(identity.getPublicKey(),
		    identity.getType().getAlgorithm());
	} catch (Exception e) {
	    throw new IdentityException("Failed to obtain public key from spki node identity", e, identity);
	}
    }

    @Override
    public byte[] ecryptPublic(EncryptOperationContext context) throws Exception {
	return CryptoUtil.encrypt(publicKey, context.getData(), context.getOpDef().getTransformation());
    }

    @Override
    public boolean verifySignature(VerifySignatureOperationContext context) throws Exception {
	SignCommOperationOutput signOutput = context.getSignOutput();
	return CryptoUtil.verifySignature(publicKey, signOutput.getData(), signOutput.getData(),
		context.getOpDef().getHashingAlgorithm());
    }

    @Override
    public String getType() {
	return identity.getType().getType();
    }

    /**
     * Return the max block size that can be encrypted
     */
    @Override
    public int getMaxSupportedEncryptedDataBlockSize() {
	return identity.getType().getKeySize()/8;
    }

}
