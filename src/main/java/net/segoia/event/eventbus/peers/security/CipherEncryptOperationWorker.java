package net.segoia.event.eventbus.peers.security;

import javax.crypto.Cipher;

public class CipherEncryptOperationWorker extends CipherBasedOperationWorker implements EncryptOperationWorker{

    public CipherEncryptOperationWorker(Cipher cipher) {
	super(cipher);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
	return getCipher().doFinal(data);
    }

}
