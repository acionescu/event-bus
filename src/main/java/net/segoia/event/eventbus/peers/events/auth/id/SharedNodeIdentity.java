package net.segoia.event.eventbus.peers.events.auth.id;

import javax.crypto.SecretKey;

public class SharedNodeIdentity extends NodeIdentity<SharedIdentityType> {
    private SecretKey secretKey;

    public SharedNodeIdentity(SharedIdentityType type, SecretKey secretKey) {
	super(type);
	this.secretKey = secretKey;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    
}
