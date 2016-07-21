package net.segoia.event.conditions;

import java.util.Arrays;

public abstract class AggregatedCondition extends Condition {
    protected Condition[] subconditions;

    public AggregatedCondition(String id, Condition[] subconditions) {
	super(id);
	this.subconditions = subconditions;
    }

    @Override
    public String toString() {
	return getClass() + " [getId()=" + getId() + ", subconditions=" + Arrays.toString(subconditions) + "]";
    }

}
