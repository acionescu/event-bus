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

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import net.segoia.util.crypto.CryptoUtil;

public class DefaultCryptoHelper implements CryptoHelper {

    @Override
    public byte[] base64Decode(String input) {
	return CryptoUtil.base64Decode(input);
    }

    @Override
    public byte[] base64EncodeToBytes(byte[] input) {
	return Base64.getEncoder().encode(input);
    }

    @Override
    public byte[] base64DecodeToBytes(byte[] input) {
	return Base64.getDecoder().decode(input);
    }

    @Override
    public String base64Encode(byte[] input) {
	return new String(Base64.getEncoder().encode(input), Charset.forName("UTF-8"));
    }

    @Override
    public String sha256(String input) {
	try {
	    return CryptoUtil.computeHash(input, "SHA-256");
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException("Can't compute hash",e);
	}
    }

}
