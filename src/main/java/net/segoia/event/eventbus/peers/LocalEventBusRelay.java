package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.LooseEventMatchCondition;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventListener;
import net.segoia.event.eventbus.EventTracker;
import net.segoia.event.eventbus.FilteringEventBus;

public class LocalEventBusRelay extends EventBusRelay implements EventListener{
    private FilteringEventBus bus;

    public LocalEventBusRelay(String id, EventBusNode parentNode, FilteringEventBus bus) {
	super(id, parentNode);
	this.bus = bus;
    }

    @Override
    protected void postInternally(Event event) {
	EventTracker eventTracker = bus.postEvent(event);
	if(eventTracker.hasErrors()) {
	    eventTracker.getFirstError().printStackTrace();
	}
    }

    @Override
    public void onEvent(EventContext ec) {
	onLocalEvent(ec);
    }


    @Override
    public void init() {
	registerForCondition(new LooseEventMatchCondition("EBUS:CLUSTER:",true));
    }


    @Override
    public void terminate() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void registerForCondition(Condition condition) {
	bus.registerListener(condition,this);
	
    }
    
}
