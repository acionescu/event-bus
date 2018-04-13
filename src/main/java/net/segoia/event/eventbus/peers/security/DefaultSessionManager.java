package net.segoia.event.eventbus.peers.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import net.segoia.event.eventbus.peers.comm.EncryptSymmetricOperationDef;

public class DefaultSessionManager implements SessionManager {
    private SecretKey secretKey;

    public DefaultSessionManager(SecretKey secretKey) {
	super();
	this.secretKey = secretKey;
    }

    @Override
    public EncryptOperationWorker buildEncryptWorker(EncryptSymmetricOperationDef opDef) throws Exception {
	Cipher cipher = Cipher.getInstance(opDef.getTransformation());
	cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	return new CipherEncryptOperationWorker(cipher);
    }

    @Override
    public DecryptOperationWorker buildDecryptWorker(EncryptSymmetricOperationDef opDef) throws Exception {
	Cipher cipher = Cipher.getInstance(opDef.getTransformation());
	cipher.init(Cipher.DECRYPT_MODE, secretKey);
	return new CipherDecryptOperationWorker(cipher);
    }

    @Override
    public String getIdentityType() {
	return secretKey.getAlgorithm();
    }

}
