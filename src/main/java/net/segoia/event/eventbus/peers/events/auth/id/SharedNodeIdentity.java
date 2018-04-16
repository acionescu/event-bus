package net.segoia.event.eventbus.peers.events.auth.id;

import javax.crypto.SecretKey;

public class SharedNodeIdentity extends NodeIdentity<SharedIdentityType> {
    private SecretKey secretKey;
    private byte[] iv;

    public SharedNodeIdentity(SharedIdentityType type, SecretKey secretKey, byte[] iv) {
	super(type);
	this.secretKey = secretKey;
	this.iv=iv;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public byte[] getIv() {
        return iv;
    }

    
}
