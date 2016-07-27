package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventTracker;
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

    @Override
    protected EventBusRelay buildLocalRelay(String peerId, EventBusNode peerNode) {
	return new LocalEventBusRelay(peerId, this, bus);
    }

    @Override
    protected EventTracker postInternally(Event event) {
	return bus.postEvent(event);
    }

}
