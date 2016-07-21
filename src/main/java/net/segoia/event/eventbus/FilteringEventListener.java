package net.segoia.event.eventbus;

import net.segoia.event.conditions.Condition;

public class FilteringEventListener extends SimpleEventDispatcher implements EventListener {
    private Condition condition;
    
    public FilteringEventListener(Condition condition) {
	super();
	this.condition = condition;
    }


    public void onEvent(EventContext ec) {
	if(condition.test(ec)) {
	    dispatchEvent(ec);
	}

    }


    @Override
    public void init() {
	// TODO Auto-generated method stub
	
    }


    @Override
    public void terminate() {
	// TODO Auto-generated method stub
	
    }

    
}
