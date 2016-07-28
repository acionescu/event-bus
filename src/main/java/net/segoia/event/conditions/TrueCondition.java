package net.segoia.event.conditions;

import net.segoia.event.eventbus.EventContext;

public class TrueCondition extends Condition{

    public TrueCondition() {
	super("true");
    }

    @Override
    public boolean test(EventContext input) {
	return true;
    }

}
