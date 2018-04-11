package net.segoia.event.eventbus.peers.security;

import javax.crypto.Cipher;

public class CipherBasedOperationWorker {
    private Cipher cipher;

    
    
    public CipherBasedOperationWorker(Cipher cipher) {
	super();
	this.cipher = cipher;
    }

    public Cipher getCipher() {
	return cipher;
    }

    public void setCipher(Cipher cipher) {
	this.cipher = cipher;
    }

}
