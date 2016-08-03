package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventListener;
import net.segoia.event.eventbus.FilteringEventBus;

public class LocalEventBusRelay extends EventRelay implements EventListener{
    private FilteringEventBus bus;

    public LocalEventBusRelay(String id, EventNode parentNode, FilteringEventBus bus) {
	super(id, parentNode);
	this.bus = bus;
    }

    @Override
    public void onEvent(EventContext ec) {
	onLocalEvent(ec);
    }


    @Override
    public void init() {
	
    }


    @Override
    public void cleanUp() {
	bus.removeListener(this);
	
    }

    @Override
    protected void start() {
	bus.registerListener(this,999);
	
    }
    
}
