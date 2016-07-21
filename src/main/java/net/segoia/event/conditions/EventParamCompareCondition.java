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
