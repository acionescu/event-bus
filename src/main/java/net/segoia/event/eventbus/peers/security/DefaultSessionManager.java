package net.segoia.event.eventbus.peers.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.segoia.event.eventbus.peers.vo.auth.id.SharedNodeIdentity;
import net.segoia.event.eventbus.peers.vo.comm.EncryptSymmetricOperationDef;

public class DefaultSessionManager implements SessionManager {
    private SecretKey secretKey;
    private byte[] iv;

    public DefaultSessionManager(SharedNodeIdentity sharedIdentity) {
	super();
	this.secretKey = new SecretKeySpec(sharedIdentity.getKeyBytes(),
		sharedIdentity.getType().getKeyDef().getAlgorithm());
	this.iv = sharedIdentity.getIv();
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
