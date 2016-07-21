package net.segoia.event.conditions;

import net.segoia.event.eventbus.EventContext;

public class OrCondition extends AggregatedCondition {

    public OrCondition(String id, Condition[] subconditions) {
	super(id, subconditions);
    }

    @Override
    public boolean test(EventContext input) {
	if (subconditions == null || subconditions.length == 0) {
	    return false;
	}
	for (Condition c : subconditions) {
	    if (c.test(input)) {
		return true;
	    }
	}
	return false;
    }

}
