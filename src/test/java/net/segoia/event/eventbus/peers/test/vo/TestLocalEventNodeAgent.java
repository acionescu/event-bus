package net.segoia.event.eventbus.peers.test.vo;

import java.util.ArrayList;
import java.util.List;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.LocalAgentEventNodeContext;
import net.segoia.event.eventbus.peers.LocalEventNodeAgent;
import net.segoia.event.eventbus.util.EBus;

public class TestLocalEventNodeAgent extends LocalEventNodeAgent{
    private List<Event> receivedEvents=new ArrayList<>();
    private boolean loggingOn;

    @Override
    protected void registerHandlers(LocalAgentEventNodeContext context) {
	context.addEventHandler((c)->{
	    receivedEvents.add(c.getEvent());
	    System.out.println("server agent received "+c.getEvent().getEt());
	    if(loggingOn) {
		EBus.postEvent(c.getEvent());
//		System.out.println("done logging from thread "+Thread.currentThread().getId()+ " "+c.getEvent().getEt());
	    }
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

    public boolean isLoggingOn() {
        return loggingOn;
    }

    public void setLoggingOn(boolean loggingOn) {
        this.loggingOn = loggingOn;
    }
    
    

}
