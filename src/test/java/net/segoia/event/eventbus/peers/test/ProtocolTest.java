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

import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequestEvent;
import net.segoia.event.eventbus.util.EBus;
import net.segoia.event.eventbus.util.JsonUtils;

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
    
    @Test
    public void testJson() {
	String input="{\n" + 
		"  \"data\": {\"nodeInfo\": {\n" + 
		"    \"nodeAuth\": {},\n" + 
		"    \"securityPolicy\": {\"channelsSecurity\": {\"WSS_V1\": {\"communicationPolicy\": {\n" + 
		"      \"supportedTxStrategies\": [{\n" + 
		"        \"sessionTxStrategy\": {\"operations\": [\n" + 
		"          {\n" + 
		"            \"transformation\": \"RSA/ECB/PKCS1Padding\",\n" + 
		"            \"type\": \"EP\"\n" + 
		"          },\n" + 
		"          {\n" + 
		"            \"hashingAlgorithm\": \"SHA256withRSA\",\n" + 
		"            \"type\": \"S\"\n" + 
		"          }\n" + 
		"        ]},\n" + 
		"        \"sharedIdentityType\": {\n" + 
		"          \"keyDef\": {\n" + 
		"            \"keySize\": 128,\n" + 
		"            \"algorithm\": \"AES\"\n" + 
		"          },\n" + 
		"          \"type\": \"SHARED\"\n" + 
		"        },\n" + 
		"        \"txNodeIdentityType\": {\n" + 
		"          \"keySize\": 1024,\n" + 
		"          \"type\": \"SPKI\",\n" + 
		"          \"algorithm\": \"RSA\"\n" + 
		"        },\n" + 
		"        \"rxNodeIdentityType\": {\n" + 
		"          \"keySize\": 1024,\n" + 
		"          \"type\": \"SPKI\",\n" + 
		"          \"algorithm\": \"RSA\"\n" + 
		"        },\n" + 
		"        \"directTxStrategy\": {\"operations\": [\n" + 
		"          {\n" + 
		"            \"hashingAlgorithm\": \"SHA256withRSA\",\n" + 
		"            \"type\": \"S\"\n" + 
		"          },\n" + 
		"          {\n" + 
		"            \"transformation\": \"AES/CBC/PKCS5Padding\",\n" + 
		"            \"type\": \"ES\"\n" + 
		"          }\n" + 
		"        ]}\n" + 
		"      }],\n" + 
		"      \"supportedRxStrategies\": [{\n" + 
		"        \"sessionTxStrategy\": {\"operations\": [\n" + 
		"          {\n" + 
		"            \"transformation\": \"RSA/ECB/PKCS1Padding\",\n" + 
		"            \"type\": \"EP\"\n" + 
		"          },\n" + 
		"          {\n" + 
		"            \"hashingAlgorithm\": \"SHA256withRSA\",\n" + 
		"            \"type\": \"S\"\n" + 
		"          }\n" + 
		"        ]},\n" + 
		"        \"sharedIdentityType\": {\n" + 
		"          \"keyDef\": {\n" + 
		"            \"keySize\": 128,\n" + 
		"            \"algorithm\": \"AES\"\n" + 
		"          },\n" + 
		"          \"type\": \"SHARED\"\n" + 
		"        },\n" + 
		"        \"txNodeIdentityType\": {\n" + 
		"          \"keySize\": 1024,\n" + 
		"          \"type\": \"SPKI\",\n" + 
		"          \"algorithm\": \"RSA\"\n" + 
		"        },\n" + 
		"        \"rxNodeIdentityType\": {\n" + 
		"          \"keySize\": 1024,\n" + 
		"          \"type\": \"SPKI\",\n" + 
		"          \"algorithm\": \"RSA\"\n" + 
		"        },\n" + 
		"        \"directTxStrategy\": {\"operations\": [\n" + 
		"          {\n" + 
		"            \"hashingAlgorithm\": \"SHA256withRSA\",\n" + 
		"            \"type\": \"S\"\n" + 
		"          },\n" + 
		"          {\n" + 
		"            \"transformation\": \"AES/CBC/PKCS5Padding\",\n" + 
		"            \"type\": \"ES\"\n" + 
		"          }\n" + 
		"        ]}\n" + 
		"      }]\n" + 
		"    }}}},\n" + 
		"    \"nodeId\": \"0263896e-4e38-4782-b0dc-8592bdea3481\"\n" + 
		"  }},\n" + 
		"  \"forwardTo\": [],\n" + 
		"  \"params\": {},\n" + 
		"  \"tags\": [],\n" + 
		"  \"et\": \"PEER:AUTH:REQUEST\",\n" + 
		"  \"spawnedEventsIds\": [],\n" + 
		"  \"headerParams\": {},\n" + 
		"  \"scope\": \"PEER\",\n" + 
		"  \"name\": \"REQUEST\",\n" + 
		"  \"header\": {\n" + 
		"    \"spawnedEventsIds\": [],\n" + 
		"    \"relayedBy\": [],\n" + 
		"    \"forwardTo\": [],\n" + 
		"    \"params\": {},\n" + 
		"    \"tags\": []\n" + 
		"  },\n" + 
		"  \"id\": \"c2634f4c-b2da-4ef1-a45e-5d8ee21b5bd1\",\n" + 
		"  \"category\": \"AUTH\",\n" + 
		"  \"ts\": 1526765060535\n" + 
		"}";
	
		EBus.initialize();
	
	
		JsonUtils.fromJson(input, PeerAuthRequestEvent.class);
    }
}
