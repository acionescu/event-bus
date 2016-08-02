package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventListener;
import net.segoia.event.eventbus.FilteringEventBus;

public class LocalEventBusRelay extends EventBusRelay implements EventListener{
    private FilteringEventBus bus;

    public LocalEventBusRelay(String id, EventBusNode parentNode, FilteringEventBus bus) {
	super(id, parentNode);
	this.bus = bus;
    }

    @Override
    public void onEvent(EventContext ec) {
	onLocalEvent(ec);
    }


    @Override
    public void init() {
	bus.registerListener(this,999);
    }


    @Override
    public void terminate() {
	bus.removeListener(this);
	
    }
    
}
