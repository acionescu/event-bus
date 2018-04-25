package net.segoia.event.eventbus.peers.security;

import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import net.segoia.event.eventbus.peers.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentity;
import net.segoia.util.crypto.CryptoUtil;

public class SpkiPublicIdentityManagerImpl implements SpkiPublicIdentityManager {
    private PublicKey publicKey;
    private SpkiNodeIdentity identity;
    private String identityKey;

    public SpkiPublicIdentityManagerImpl(SpkiNodeIdentity identity) {
	this.identity = identity;
	try {
	    String pubKeyString = identity.getPublicKey();
	    publicKey = CryptoUtil.getPublicKeyFromBase64EncodedString(pubKeyString,
		    identity.getType().getAlgorithm());
	    
	    identityKey = CryptoUtil.computeHash(pubKeyString, "SHA-256");
	} catch (Exception e) {
	    throw new IdentityException("Failed to obtain public key from spki node identity", e, identity);
	}
    }

//    @Override
//    public byte[] ecryptPublic(EncryptOperationContext context) throws Exception {
//	return CryptoUtil.encrypt(publicKey, context.getData(), context.getOpDef().getTransformation());
//    }
//
//    @Override
//    public boolean verifySignature(VerifySignatureOperationContext context) throws Exception {
//	SignCommOperationOutput signOutput = context.getSignOutput();
//	return CryptoUtil.verifySignature(publicKey, signOutput.getData(), signOutput.getData(),
//		context.getOpDef().getHashingAlgorithm());
//    }

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
    
    

    @Override
    public EncryptOperationWorker buildEncryptPublicWorker(EncryptWithPublicCommOperationDef opDef) throws Exception {
	Cipher cipher = Cipher.getInstance(opDef.getTransformation());
	cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	return new CipherEncryptOperationWorker(cipher);
    }

    @Override
    public VerifySignatureOperationWorker buildVerifySignatureWorker(SignCommOperationDef opDef) throws Exception{
	Signature sig = Signature.getInstance(opDef.getHashingAlgorithm());
	sig.initVerify(publicKey);
	return new DefaultVerifySignatureOperationWorker(sig);
    }

    public SpkiNodeIdentity getIdentity() {
        return identity;
    }

    @Override
    public String getIdentityKey() {
	return identityKey;
    }

  

}
