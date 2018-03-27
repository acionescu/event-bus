package net.segoia.event.eventbus;

import net.segoia.event.conditions.Condition;

public class BlockingFilteringEventProcessor extends FilteringEventProcessor {
    
    

    public BlockingFilteringEventProcessor() {
	super();
	// TODO Auto-generated constructor stub
    }

    public BlockingFilteringEventProcessor(EventDispatcher eventDispatcher) {
	super(eventDispatcher);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected FilteringEventDispatcher createEventDispatcherForCondition(Condition condition) {
	return new BlockingFilteringEventDispatcher(condition);
    }

}
