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
package net.segoia.event.eventbus.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventHandle;
import net.segoia.event.eventbus.EventListener;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.util.data.ListMap;

public class TestEventListener implements EventListener {
    /**
     * If set to true will create an echo event for every event received, setting it as cause
     */
    private boolean echoOn;
    
    /**
     * A way to identify this listener
     */
    private String listenerKey;

    private Map<Event, Long> events = new HashMap<>();
    
    private ListMap<String,Event> eventsByType = new ListMap<>();
    
    private Condition testCondition;
    
    private boolean conditionSatisfied;

    @Override
    public void onEvent(EventContext ec) {
	
	Event event = ec.getEvent();
	if(echoOn && event.getScope().equals("TEST")) {
	    /**
	     * Don't allow to echo test events cause we'll be dying a recursive death
	     */
	    return;
	}
	
	synchronized (events) {

	    events.put(event, System.currentTimeMillis());
	}
	
	synchronized (eventsByType) {
	    eventsByType.add(event.getEt(),event);
	}
	
	if(testCondition != null ) {
	    conditionSatisfied = testCondition.test(ec);
	}
	

	if (echoOn) {
	    EventHandle eh = Events.builder().spawnFrom(event).scope("TEST").category("TEST").name("ECHO").topic(listenerKey).getHandle();
	    eh.post();
	}

	try {
	    Thread.sleep(1);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public boolean hasReceivedEvent(Event e) {
	return events.containsKey(e);
    }

    public Long getReceivedTs(Event e) {
	return events.get(e);
    }
    
    public Collection<Event> getEventsForType(String type){
	return eventsByType.get(type);
    }

    @Override
    public void init() {
	// TODO Auto-generated method stub

    }

    @Override
    public void terminate() {
	// TODO Auto-generated method stub

    }

    /**
     * @return the echoOn
     */
    public boolean isEchoOn() {
        return echoOn;
    }

    /**
     * @return the listenerKey
     */
    public String getListenerKey() {
        return listenerKey;
    }

    /**
     * @return the testCondition
     */
    public Condition getTestCondition() {
        return testCondition;
    }

    /**
     * @param echoOn the echoOn to set
     */
    public void setEchoOn(boolean echoOn) {
        this.echoOn = echoOn;
    }

    /**
     * @param listenerKey the listenerKey to set
     */
    public void setListenerKey(String listenerKey) {
        this.listenerKey = listenerKey;
    }

    /**
     * @param testCondition the testCondition to set
     */
    public void setTestCondition(Condition testCondition) {
        this.testCondition = testCondition;
    }

    /**
     * @return the conditionSatisfied
     */
    public boolean isConditionSatisfied() {
        return conditionSatisfied;
    }

    /**
     * @return the events
     */
    public Map<Event, Long> getEvents() {
        return events;
    }

}
