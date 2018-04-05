package net.segoia.event.eventbus.peers.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.DefaultEventNode;
import net.segoia.event.eventbus.peers.EventNode;
import net.segoia.event.eventbus.peers.LocalAgentEventNodeContext;
import net.segoia.event.eventbus.peers.LocalEventNodeAgent;
import net.segoia.event.eventbus.peers.events.NewPeerEvent;
import net.segoia.event.eventbus.peers.events.PeerLeftEvent;
import net.segoia.event.eventbus.peers.events.auth.NodeAuth;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentityType;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequest;
import net.segoia.event.eventbus.peers.test.vo.ClientTestEventTransceiver;
import net.segoia.event.eventbus.peers.test.vo.ServerTestEventTransceiver;
import net.segoia.event.eventbus.peers.test.vo.TestLocalEventNodeAgent;
import net.segoia.event.eventbus.util.EBus;

public class EventNodeTest {

    @Test
    public void testLoad() {
	EventNode mainNode = EBus.getMainNode();

	Assert.assertNotNull(mainNode);

	Assert.assertEquals(mainNode.getClass(), DefaultEventNode.class);

	Assert.assertTrue(mainNode.isInitialized());
    }

    @Test
    public void testRegisterLocalAgent() {
	EventNode mainNode = EBus.getMainNode();

	final Event testEvent = new Event("test:test:event");

	final List<Event> gotEvents = new ArrayList<>();

	LocalEventNodeAgent localAgent = new LocalEventNodeAgent() {

	    @Override
	    protected void agentInit() {
		postEvent(testEvent);
	    }

	    @Override
	    public void terminate() {
		// TODO Auto-generated method stub

	    }

	    @Override
	    protected void registerHandlers(LocalAgentEventNodeContext context) {
		context.addEventHandler((c) -> {

		    gotEvents.add(c.getEvent());
		});

	    }

	    @Override
	    protected void config() {
		// TODO Auto-generated method stub

	    }
	};

	mainNode.registerLocalAgent(localAgent);

	EBus.waitToProcessAllOnMainLoop();

	Assert.assertTrue(testEvent.equals(gotEvents.get(0)));
    }

    
    @Test
    public void testPeering() {
	/* build a peer node with the same configuration as the main node */
	final EventNode peerNode = EBus.loadNode("main_node.json").getNode();
	
	final EventNode mainNode = EBus.getMainNode();
	
	ServerTestEventTransceiver serverTransceiver = new ServerTestEventTransceiver(mainNode);
	
	ClientTestEventTransceiver clientTransceiver = new ClientTestEventTransceiver(serverTransceiver);
	
	clientTransceiver.setSendAsync(true);
	serverTransceiver.setSendAsync(true);
	
	TestLocalEventNodeAgent serverLocalAgent = new TestLocalEventNodeAgent();
	mainNode.registerLocalAgent(serverLocalAgent);
	
	/* initiate peering */
	peerNode.registerToPeer(new ConnectToPeerRequest(clientTransceiver));
	
	/* wait for all events to pe handled */
	EBus.waitToProcessAllOnMainLoop();
	
	List<Event> receivedEvents = serverLocalAgent.getReceivedEvents();
	
	Assert.assertTrue(receivedEvents.size() > 0);
	
	/* the last event received should be the new peer event */
	Event lastReceivedEvent = receivedEvents.get(receivedEvents.size()-1);
	Assert.assertTrue(lastReceivedEvent instanceof NewPeerEvent);
	
	NewPeerEvent newPeerEvent = (NewPeerEvent)lastReceivedEvent;
	
	/* simulate the peer node leaving */
	clientTransceiver.terminate();
	/* wait for all events to be handled */
	EBus.waitToProcessAllOnMainLoop();
	
	/* check if the peer left event is triggered on server side*/
	lastReceivedEvent = receivedEvents.get(receivedEvents.size()-1);
	Assert.assertTrue(lastReceivedEvent instanceof PeerLeftEvent);
	
	PeerLeftEvent peerLeftEvent = (PeerLeftEvent)lastReceivedEvent;
	/* check that it's the same peer that was added */
	Assert.assertTrue(newPeerEvent.getData().getPeerId().equals(peerLeftEvent.getData().getPeerId()));
	
    }
    
    @Test
    public void testPeeringWithSecurity() {
	
	final EventNode peerNode1 = EBus.loadNode("peer1_node.json").getNode();
	
	NodeAuth nodeAuth = peerNode1.getNodeInfo().getNodeAuth();
	
	List<? extends NodeIdentity<? extends NodeIdentityType>> nodeIdentities = nodeAuth.getIdentities();
	/* we should have at least one identity */
	Assert.assertTrue(nodeIdentities.size() > 0);
	
	Assert.assertTrue(nodeIdentities.get(0) instanceof SpkiNodeIdentity);
	
//	SpkiNodeIdentity peer1I1 = (SpkiNodeIdentity)nodeIdentities.get(0);
	
	final EventNode peerNode2 = EBus.loadNode("peer2_node.json").getNode();
	
	
	ServerTestEventTransceiver serverTransceiver = new ServerTestEventTransceiver(peerNode1);
	
	ClientTestEventTransceiver clientTransceiver = new ClientTestEventTransceiver(serverTransceiver);
	
	clientTransceiver.setSendAsync(true);
	serverTransceiver.setSendAsync(true);
	
	TestLocalEventNodeAgent serverLocalAgent = new TestLocalEventNodeAgent();
	
	peerNode1.registerLocalAgent(serverLocalAgent);
	serverLocalAgent.setLoggingOn(true);
	
	
	/* initiate peering */
	peerNode2.registerToPeer(new ConnectToPeerRequest(clientTransceiver));
	
	
	/* wait for all events to pe handled */
	EBus.waitToProcessAllOnMainLoop(20);
	
	System.out.println(serverLocalAgent.getReceivedEvents().size());
    }
}
