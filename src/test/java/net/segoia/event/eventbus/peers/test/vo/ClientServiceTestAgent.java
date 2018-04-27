package net.segoia.event.eventbus.peers.test.vo;

import net.segoia.event.eventbus.peers.EventNode;
import net.segoia.event.eventbus.peers.GlobalEventNodeAgent;
import net.segoia.event.eventbus.peers.events.PeerAcceptedEvent;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdIssuedEvent;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdRequest;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdRequestEvent;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiFullNodeIdentity;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequest;
import net.segoia.event.eventbus.peers.events.bind.DisconnectFromPeerRequest;
import net.segoia.event.eventbus.services.EventNodeServiceRef;

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
	});

	context.addEventHandler(ServiceAccessIdIssuedEvent.class, (c) -> {
	    NodeIdentity<?> accessIdentity = c.getEvent().getData().getAccessIdentity();
	    System.out.println("We've got a service access id " + accessIdentity);
	    serviceAccessIdentity = (SpkiFullNodeIdentity) accessIdentity;

	    /* terminate old connection, start a new one with the obtained service access id */
	    context.disconnectFromPeer(new DisconnectFromPeerRequest(c.getEvent().from()));
	    
	    
	    
	});
    }

}
