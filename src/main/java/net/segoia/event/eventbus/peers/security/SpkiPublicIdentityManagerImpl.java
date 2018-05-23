/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.eventbus.peers.security;

import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import net.segoia.event.eventbus.peers.core.IdentityException;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.peers.vo.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.vo.comm.SignCommOperationDef;
import net.segoia.util.crypto.CryptoUtil;

public class SpkiPublicIdentityManagerImpl implements SpkiPublicIdentityManager {
    private PublicKey publicKey;
    private SpkiNodeIdentity identity;
    private String identityKey;

    public SpkiPublicIdentityManagerImpl(SpkiNodeIdentity identity) {
	this.identity = identity;
	try {
	    String pubKeyString = identity.getPublicKey();
	    publicKey = CryptoUtil.getPublicKeyFromBase64EncodedString(pubKeyString,
		    identity.getType().getAlgorithm());
	    
	    identityKey = CryptoUtil.computeHash(pubKeyString, "SHA-256");
	} catch (Exception e) {
	    throw new IdentityException("Failed to obtain public key from spki node identity", e, identity);
	}
    }

//    @Override
//    public byte[] ecryptPublic(EncryptOperationContext context) throws Exception {
//	return CryptoUtil.encrypt(publicKey, context.getData(), context.getOpDef().getTransformation());
//    }
//
//    @Override
//    public boolean verifySignature(VerifySignatureOperationContext context) throws Exception {
//	SignCommOperationOutput signOutput = context.getSignOutput();
//	return CryptoUtil.verifySignature(publicKey, signOutput.getData(), signOutput.getData(),
//		context.getOpDef().getHashingAlgorithm());
//    }

    @Override
    public String getType() {
	return identity.getType().getType();
    }

    /**
     * Return the max block size that can be encrypted
     */
    @Override
    public int getMaxSupportedEncryptedDataBlockSize() {
	return identity.getType().getKeySize()/8;
    }
    
    

    @Override
    public EncryptOperationWorker buildEncryptPublicWorker(EncryptWithPublicCommOperationDef opDef) throws Exception {
	Cipher cipher = Cipher.getInstance(opDef.getTransformation(),BouncyCastleProvider.PROVIDER_NAME);
	cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	return new CipherEncryptOperationWorker(cipher);
    }

    @Override
    public VerifySignatureOperationWorker buildVerifySignatureWorker(SignCommOperationDef opDef) throws Exception{
	Signature sig = Signature.getInstance(opDef.getHashingAlgorithm(),BouncyCastleProvider.PROVIDER_NAME);
	sig.initVerify(publicKey);
	return new DefaultVerifySignatureOperationWorker(sig);
    }

    public SpkiNodeIdentity getIdentity() {
        return identity;
    }

    @Override
    public String getIdentityKey() {
	return identityKey;
    }

  

}
