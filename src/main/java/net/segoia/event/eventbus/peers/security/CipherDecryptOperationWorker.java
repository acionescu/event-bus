package net.segoia.event.eventbus.peers.security;

import javax.crypto.Cipher;

public class CipherDecryptOperationWorker extends CipherBasedOperationWorker implements DecryptOperationWorker{

    public CipherDecryptOperationWorker(Cipher cipher) {
	super(cipher);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
	return getCipher().doFinal(data);
	
    }
    
}
