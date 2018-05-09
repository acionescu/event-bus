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
