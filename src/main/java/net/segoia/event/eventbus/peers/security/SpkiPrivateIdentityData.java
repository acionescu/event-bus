package net.segoia.event.eventbus.peers.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import net.segoia.event.eventbus.peers.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentity;
import net.segoia.util.crypto.CryptoUtil;

public class SpkiPrivateIdentityData extends PrivateIdentityData<SpkiNodeIdentity> implements SpkiPrivateIdentityManager{
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public SpkiPrivateIdentityData(PrivateKey privateKey, PublicKey publicKey, String algorithm, int keySize) {
	super();
	this.privateKey = privateKey;
	this.publicKey = publicKey;
	
	/* set the public node identity from public key */
	setPublicNodeIdentity(new SpkiNodeIdentity(CryptoUtil.base64Encode(publicKey.getEncoded()), algorithm,keySize));
    }

    public PrivateKey getPrivateKey() {
	return privateKey;
    }

    public PublicKey getPublicKey() {
	return publicKey;
    }

//    @Override
//    public byte[] sign(SignCommOperationContext context) throws Exception {
//	SignCommOperationDef opDef = context.getOpDef();
//	return CryptoUtil.sign(privateKey, context.getData(), opDef.getHashingAlgorithm());
//    }
//
//    @Override
//    public byte[] decryptPrivate(DecryptOperationContext context) throws Exception {
//	return CryptoUtil.decrypt(privateKey, context.getData(), context.getOpDef().getTransformation());
//    }

    @Override
    public SignOperationWorker buildSignWorker(SignCommOperationDef opDef) throws Exception {
	Signature sig = Signature.getInstance(opDef.getHashingAlgorithm());
	sig.initSign(privateKey);
	//TODO: reuse the signature for the same algorithm;
	return new DefaultSignOperationWorker(sig);
    }

    @Override
    public DecryptOperationWorker buildPrivateDecryptWorker(EncryptWithPublicCommOperationDef opDef) throws Exception {
	Cipher cipher = Cipher.getInstance(opDef.getTransformation());
	cipher.init(Cipher.DECRYPT_MODE, privateKey);
	return new CipherDecryptOperationWorker(cipher);
    }

}
