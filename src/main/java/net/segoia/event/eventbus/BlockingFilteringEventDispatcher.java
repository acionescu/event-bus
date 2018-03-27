package net.segoia.event.eventbus;

import net.segoia.event.conditions.Condition;

public class BlockingFilteringEventDispatcher extends FilteringEventDispatcher{

    public BlockingFilteringEventDispatcher(Condition condition) {
	super(condition, new BlockingEventDispatcher());
	
    }

}
