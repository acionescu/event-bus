package net.segoia.event.eventbus.peers.test;

import org.junit.Test;

import junit.framework.Assert;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.conditions.TrueCondition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.peers.LocalEventBusNode;
import net.segoia.event.eventbus.test.TestEventListener;
import net.segoia.event.eventbus.util.EBus;

public class DistributionTest {

    
    @Test
    public void simpleDistributionTest() {
	/**
	 * Create two buses, peer them together, then see if events reach out from one to the other
	 */
	
	FilteringEventBus b1 = new FilteringEventBus();
	FilteringEventBus b2 = new FilteringEventBus();
	
	LocalEventBusNode node1 = new LocalEventBusNode(b1);
	LocalEventBusNode node2 = new LocalEventBusNode(b2);
	
	LocalEventBusNode mainNode = new LocalEventBusNode(EBus.instance());
	
	/* register main node on all events */
	node1.registerPeer(mainNode, new TrueCondition());
	node2.registerPeer(mainNode , new TrueCondition());
	
	node1.registerPeer(node2);
	
	TestEventListener tl1 = new TestEventListener();
	b1.registerListener(tl1);
	
	TestEventListener tl2 = new TestEventListener();
	b2.registerListener(tl2);
	
	Event e1 = Events.builder().system().message().name("test").build();
	
	b1.postEvent(e1);
	
	Assert.assertTrue(tl1.hasReceivedEvent(e1));
	
	Assert.assertFalse(tl2.hasReceivedEvent(e1));
	
	
	/* register node2 for the type of event e1 */
	StrictEventMatchCondition tec = new StrictEventMatchCondition(e1.getEt());
	node1.registerPeer(node2,tec);
	
	Event e2 = Events.builder().system().message().name("test").build();
	
	b1.postEvent(e2);
	
	/* now we should receive the event */
	Assert.assertTrue(tl2.hasReceivedEvent(e2));
	
	Event e3 = Events.builder().system().message().name("test").build();
	
	b2.postEvent(e3);
	
	Assert.assertFalse(tl1.hasReceivedEvent(e3));
	
	node2.registerPeer(node1, tec);
	
	b2.postEvent(e3);
	
	Assert.assertTrue(tl1.hasReceivedEvent(e3));
	
    }
    
}
