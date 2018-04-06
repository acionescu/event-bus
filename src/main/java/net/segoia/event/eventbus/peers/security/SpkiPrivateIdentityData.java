package net.segoia.event.eventbus.peers.security;

import java.security.PrivateKey;
import java.security.PublicKey;

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

    @Override
    public byte[] sign(SignCommOperationContext context) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public byte[] decryptPrivate(DecryptOperationContext context) {
	// TODO Auto-generated method stub
	return null;
    }

}
