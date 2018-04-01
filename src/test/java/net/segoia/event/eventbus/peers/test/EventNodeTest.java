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

	EBus.processAllFromMainLoopAndStop();

	Assert.assertTrue(testEvent.equals(gotEvents.get(0)));
    }

}
