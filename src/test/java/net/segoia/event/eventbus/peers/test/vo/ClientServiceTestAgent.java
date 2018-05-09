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
package net.segoia.event.eventbus.peers.test.vo;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import net.segoia.event.eventbus.peers.EventNode;
import net.segoia.event.eventbus.peers.GlobalEventNodeAgent;
import net.segoia.event.eventbus.peers.core.PrivateIdentityData;
import net.segoia.event.eventbus.peers.events.PeerAcceptedEvent;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdIssuedEvent;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdRequestEvent;
import net.segoia.event.eventbus.peers.security.SpkiPrivateIdentityData;
import net.segoia.event.eventbus.peers.vo.auth.ServiceAccessIdRequest;
import net.segoia.event.eventbus.peers.vo.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiFullNodeIdentity;
import net.segoia.event.eventbus.peers.vo.bind.ConnectToPeerRequest;
import net.segoia.event.eventbus.peers.vo.bind.DisconnectFromPeerRequest;
import net.segoia.event.eventbus.peers.vo.session.KeyDef;
import net.segoia.event.eventbus.vo.services.EventNodeServiceRef;
import net.segoia.util.crypto.CryptoUtil;

public class ClientServiceTestAgent extends GlobalEventNodeAgent {
    private EventNode serverEventNode;
    private SpkiFullNodeIdentity serviceAccessIdentity;

    private ClientTestEventTransceiver clientTransceiver;

    public ClientServiceTestAgent(EventNode serverEventNode) {
	super();
	this.serverEventNode = serverEventNode;
    }

    @Override
    protected void agentInit() {
	ServerTestEventTransceiver serverTransceiver = new ServerTestEventTransceiver(serverEventNode);

	clientTransceiver = new ClientTestEventTransceiver(serverTransceiver);

	clientTransceiver.setSendAsync(true);
	serverTransceiver.setSendAsync(true);

	/* initiate peering */
	context.registerToPeer(new ConnectToPeerRequest(clientTransceiver));

    }

    @Override
    public void terminate() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void config() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void registerHandlers() {
	context.addEventHandler((c) -> {
	    System.out.println("Got event " + c.getEvent().toJson());
	});

	context.addEventHandler(PeerAcceptedEvent.class, (c) -> {
	    String peerId = c.getEvent().getData().getPeerId();

	    // Event event = new Event("hello:hello:encrypted");
	    // context.forwardTo(event, c.getEvent().getData().getPeerId());
	    // context.forwardTo(event, c.getEvent().getData().getPeerId());

	    if (serviceAccessIdentity == null) {
		System.out.println("Request service access id");
		ServiceAccessIdRequestEvent serviceAccessIdRequestEvent = new ServiceAccessIdRequestEvent(
			new ServiceAccessIdRequest(new EventNodeServiceRef("TEST_SERVICE", 0)));
		context.forwardTo(serviceAccessIdRequestEvent, peerId);
	    }
	    else {
		System.out.println("Successfull connect with service access id");
	    }
	});

	context.addEventHandler(ServiceAccessIdIssuedEvent.class, (c) -> {
	    NodeIdentity<?> accessIdentity = c.getEvent().getData().getAccessIdentity();
	    System.out.println("We've got a service access id " + accessIdentity);
	    serviceAccessIdentity = (SpkiFullNodeIdentity) accessIdentity;

	    /* terminate old connection, start a new one with the obtained service access id */
	    context.disconnectFromPeer(new DisconnectFromPeerRequest(c.getEvent().from()));

	    List<PrivateIdentityData<?>> ourIdentities = new ArrayList<>();

	    String privateKeyString = serviceAccessIdentity.getPrivateKey();
	    String publicKeyString = serviceAccessIdentity.getPubKey();

	    KeyDef keyDef = serviceAccessIdentity.getType().getKeyDef();

	    try {
		PrivateKey privateKey = CryptoUtil.getPrivateKeyFromBase64EncodedString(privateKeyString,
			keyDef.getAlgorithm());
		PublicKey publicKey = CryptoUtil.getPublicKeyFromBase64EncodedString(publicKeyString,
			keyDef.getAlgorithm());

		SpkiPrivateIdentityData spkiPrivateIdentityData = new SpkiPrivateIdentityData(privateKey, publicKey,
			keyDef.getAlgorithm(), keyDef.getKeySize());

		ourIdentities.add(spkiPrivateIdentityData);

		ConnectToPeerRequest newConnectRequest = new ConnectToPeerRequest(clientTransceiver, ourIdentities);
		
		context.registerToPeer(newConnectRequest);

	    } catch (Exception ex) {
		ex.printStackTrace();
	    }

	});
    }

}
