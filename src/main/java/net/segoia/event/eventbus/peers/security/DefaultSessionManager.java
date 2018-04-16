package net.segoia.event.eventbus.peers.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import net.segoia.event.eventbus.peers.comm.EncryptSymmetricOperationDef;

public class DefaultSessionManager implements SessionManager {
    private SecretKey secretKey;
    private byte[] iv;

    public DefaultSessionManager(SecretKey secretKey, byte[] iv) {
	super();
	this.secretKey = secretKey;
	this.iv =iv;
    }

    @Override
    public EncryptOperationWorker buildEncryptWorker(EncryptSymmetricOperationDef opDef) throws Exception {
	Cipher cipher = Cipher.getInstance(opDef.getTransformation());
	cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
	return new CipherEncryptOperationWorker(cipher);
    }

    @Override
    public DecryptOperationWorker buildDecryptWorker(EncryptSymmetricOperationDef opDef) throws Exception {
	Cipher cipher = Cipher.getInstance(opDef.getTransformation());
	cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
	return new CipherDecryptOperationWorker(cipher);
    }

    @Override
    public String getIdentityType() {
	return secretKey.getAlgorithm();
    }

}
