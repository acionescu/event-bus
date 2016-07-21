package net.segoia.event.conditions;

import net.segoia.event.eventbus.EventContext;

public class AndCondition extends AggregatedCondition {

    public AndCondition(String id, Condition... subconditions) {
	super(id, subconditions);
    }

    @Override
    public boolean test(EventContext eventContext) {
	for (Condition f : subconditions) {
	    if (!f.test(eventContext)) {
		return false;
	    }
	}

	return true;
    }

}
