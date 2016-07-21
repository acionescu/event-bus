package net.segoia.event.conditions;

import net.segoia.event.eventbus.EventContext;

public class NotCondition extends AggregatedCondition{

    public NotCondition(String id, Condition[] subconditions) {
	super(id, subconditions);
    }
    
    public NotCondition(String id, Condition cond){
	super(id,new Condition[]{cond});
    }
    

    @Override
    public boolean test(EventContext input) {
	if (subconditions == null || subconditions.length == 0) {
	    return true;
	}
	/* negating the result of the first condition */
	return !subconditions[0].test(input);
    }

}
