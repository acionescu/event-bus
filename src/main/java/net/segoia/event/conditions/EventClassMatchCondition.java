package net.segoia.event.conditions;

import net.segoia.event.eventbus.EventContext;
import net.segoia.util.data.reflection.ReflectionUtility;

public class EventClassMatchCondition extends Condition{
    private Class<?> type;

    public EventClassMatchCondition(Class<?> type) {
	super(type.getName());
	this.type = type;
    }

    @Override
    public boolean test(EventContext input) {
	return ReflectionUtility.checkInstanceOf(input.getEvent().getClass(), type);
    }

}
