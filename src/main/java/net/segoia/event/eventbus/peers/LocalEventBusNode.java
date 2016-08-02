package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.InternalEventTracker;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.util.EBus;

public class LocalEventBusNode extends EventBusNode{
    private FilteringEventBus bus;
    
    public LocalEventBusNode() {
	this(EBus.instance());
    }

    public LocalEventBusNode(FilteringEventBus bus) {
	this.bus = bus;
    }
    
    

    public LocalEventBusNode(FilteringEventBus bus, EventBusNodeConfig config) {
	super(config);
	this.bus = bus;
    }

    @Override
    protected EventBusRelay buildLocalRelay(String peerId) {
	return new LocalEventBusRelay(peerId, this, bus );
    }

    @Override
    protected InternalEventTracker postInternally(Event event) {
	InternalEventTracker eventTracker = bus.postEvent(event);
	if(eventTracker.hasErrors()) {
	    eventTracker.getFirstError().printStackTrace();
	}
	return eventTracker;
    }

    @Override
    protected void init() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void terminate() {
	
    }

    @Override
    protected void handleRemoteEvent(PeerEventContext pc) {
	postInternally(pc.getEvent());
    }

    
    
}
