package net.segoia.event.eventbus.peers.test.vo;

import java.util.ArrayList;
import java.util.List;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.LocalAgentEventNodeContext;
import net.segoia.event.eventbus.peers.LocalEventNodeAgent;

public class TestLocalEventNodeAgent extends LocalEventNodeAgent{
    private List<Event> receivedEvents=new ArrayList<>();

    @Override
    protected void registerHandlers(LocalAgentEventNodeContext context) {
	context.addEventHandler((c)->{
	    receivedEvents.add(c.getEvent());
	});
    }

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

    public List<Event> getReceivedEvents() {
        return receivedEvents;
    }
    
    

}
