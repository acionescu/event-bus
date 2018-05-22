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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import net.segoia.event.eventbus.peers.core.PrivateIdentityData;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.peers.vo.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.vo.comm.SignCommOperationDef;
import net.segoia.util.crypto.CryptoUtil;

public class SpkiPrivateIdentityData extends PrivateIdentityData<SpkiNodeIdentity> implements SpkiPrivateIdentityManager{
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public SpkiPrivateIdentityData(PrivateKey privateKey, PublicKey publicKey, String algorithm, int keySize) {
	super();
	this.privateKey = privateKey;
	this.publicKey = publicKey;
	
	/* set the public node identity from public key */
	setPublicNodeIdentity(new SpkiNodeIdentity(CryptoUtil.base64Encode(publicKey.getEncoded()), algorithm,keySize));
    }

    public PrivateKey getPrivateKey() {
	return privateKey;
    }

    public PublicKey getPublicKey() {
	return publicKey;
    }

//    @Override
//    public byte[] sign(SignCommOperationContext context) throws Exception {
//	SignCommOperationDef opDef = context.getOpDef();
//	return CryptoUtil.sign(privateKey, context.getData(), opDef.getHashingAlgorithm());
//    }
//
//    @Override
//    public byte[] decryptPrivate(DecryptOperationContext context) throws Exception {
//	return CryptoUtil.decrypt(privateKey, context.getData(), context.getOpDef().getTransformation());
//    }

    @Override
    public SignOperationWorker buildSignWorker(SignCommOperationDef opDef) throws Exception {
	Signature sig = Signature.getInstance(opDef.getHashingAlgorithm(),BouncyCastleProvider.PROVIDER_NAME);
	System.out.println("using signature "+sig.getClass());
	sig.initSign(privateKey);
	//TODO: reuse the signature for the same algorithm;
	return new DefaultSignOperationWorker(sig);
    }

    @Override
    public DecryptOperationWorker buildPrivateDecryptWorker(EncryptWithPublicCommOperationDef opDef) throws Exception {
	Cipher cipher = Cipher.getInstance(opDef.getTransformation());
	cipher.init(Cipher.DECRYPT_MODE, privateKey);
	return new CipherDecryptOperationWorker(cipher);
    }

}
