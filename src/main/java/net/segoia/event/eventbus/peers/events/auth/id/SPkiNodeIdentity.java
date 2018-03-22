package net.segoia.event.eventbus.peers.events.auth.id;

public class SPkiNodeIdentity extends NodeIdentity<SpkiNodeIdentityType> {
    private String publicKey;

    public String getPublicKey() {
	return publicKey;
    }

    public void setPublicKey(String publicKey) {
	this.publicKey = publicKey;
    }

}
