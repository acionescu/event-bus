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
package net.segoia.event.eventbus.peers.test;

import org.junit.Test;

public class ProtocolTest {

    @Test
    public void testIdentityKeyGeneration() {
	/*
	 * - generate random key - identity secret key
	 * - use above key and a static string ( peer id ) to generate an id token using HmacSHA256
	 * - generate random initialization vector
	 * - specify iterations
	 * - specify keySize
	 * 
	 * - send encrypted identity secret key to peer, along with iv vector and iterations
	 * - use id token as key of peer identity
	 * 
	 * 
	 * - during authentication peer sends the id token along with a newly
	 * 
	 * 
	 * - generate secret key from above using pbkdf2
	 */
    }
}
