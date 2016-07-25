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
package net.segoia.event.conditions;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;

public class EventParamCompareCondition extends Condition{
    private String param1;
    private String param2;
    
    /**
     * One of the values returned by {@link Comparable#compareTo(Object)} 
     */
    private int expected;
    
    public EventParamCompareCondition(String id, String param1, String param2, int expected) {
	super(id);
	this.param1 = param1;
	this.param2 = param2;
	this.expected = expected;
    }




    @Override
    public boolean test(EventContext input) {
	Event event = input.getEvent();
	Comparable p1v = (Comparable)event.getParam(param1);
	Comparable p2v = (Comparable)event.getParam(param2);
	
	if(p1v == null){
	    if(p2v == null && expected == 0){
		return true;
	    }
	    return false;
	}
	else if(p2v == null){
	    return false;
	}
	
	return p1v.compareTo(p2v) == expected;
	
    }

}
