package net.segoia.event.eventbus.peers.events.auth.id;

public class SpkiNodeIdentity extends NodeIdentity<SpkiNodeIdentityType> {
    private String publicKey;

    public SpkiNodeIdentity(String publicKey, String algorithm, int keySize) {
	super(new SpkiNodeIdentityType(algorithm, keySize));
	this.publicKey = publicKey;
    }

    public String getPublicKey() {
	return publicKey;
    }

    public void setPublicKey(String publicKey) {
	this.publicKey = publicKey;
    }

}
