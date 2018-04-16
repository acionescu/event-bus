package net.segoia.event.eventbus.peers.test.vo;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.GlobalEventNodeAgent;
import net.segoia.event.eventbus.peers.events.PeerAcceptedEvent;

public class TestGlobalEventNodeAgent extends GlobalEventNodeAgent{

    @Override
    protected void agentInit() {
	// TODO Auto-generated method stub
	
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
	context.addEventHandler((c)->{
	    System.out.println("Got event "+c.getEvent().toJson());
	});
	
	context.addEventHandler(PeerAcceptedEvent.class, (c)->{
	    System.out.println("helooooo");
	    Event event = new Event("hello:hello:encrypted");
	    context.forwardTo(event, c.getEvent().getData().getPeerId());
	    context.forwardTo(event, c.getEvent().getData().getPeerId());
	});
    }

}
