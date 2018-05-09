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

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import net.segoia.util.crypto.CryptoUtil;

public class SpkiPrivateIdentityDataLoader implements PrivateIdentityDataLoader<SpkiPrivateIdentityData> {
    private SpkiIdentityDef indentityDef;

    private SpkiPrivateIdentityData data;

    @Override
    public void load() {
	try {
	    KeyPair keyPair = CryptoUtil.loadKeys(indentityDef.getAlgorithm(), indentityDef.getPath(),
		    indentityDef.getName());

	    data = new SpkiPrivateIdentityData(keyPair.getPrivate(), keyPair.getPublic(), indentityDef.getAlgorithm(),
		    indentityDef.getKeySize());
	} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {

	}
    }

    public SpkiIdentityDef getIndentityDef() {
	return indentityDef;
    }

    public void setIndentityDef(SpkiIdentityDef indentityDef) {
	this.indentityDef = indentityDef;
    }

    public SpkiPrivateIdentityData getData() {
	return data;
    }

}
