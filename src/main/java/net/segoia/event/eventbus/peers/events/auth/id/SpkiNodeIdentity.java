package net.segoia.event.eventbus.peers.events.auth.id;

public class SpkiNodeIdentity extends NodeIdentity<SpkiNodeIdentityType> {
    private String publicKey;

    public SpkiNodeIdentity() {
	super(new SpkiNodeIdentityType());
    }

    public String getPublicKey() {
	return publicKey;
    }

    public void setPublicKey(String publicKey) {
	this.publicKey = publicKey;
    }

}
