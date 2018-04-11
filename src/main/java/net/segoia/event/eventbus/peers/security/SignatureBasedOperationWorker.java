package net.segoia.event.eventbus.peers.security;

import java.security.Signature;

public class SignatureBasedOperationWorker {
    private Signature signature;

    public SignatureBasedOperationWorker(Signature signature) {
	super();
	this.signature = signature;
    }

    public Signature getSignature() {
	return signature;
    }

    public void setSignature(Signature signature) {
	this.signature = signature;
    }

}
