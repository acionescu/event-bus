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

import java.util.ArrayList;
import java.util.List;

import net.segoia.event.eventbus.EBusVM;
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
		EBusVM.getInstance().postSystemEvent(c.getEvent());
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
