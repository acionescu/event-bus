package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.InternalEventTracker;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.util.EBus;

public class LocalEventBusNode extends EventNode{
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
    protected EventRelay buildLocalRelay(String peerId) {
	return new LocalEventBusRelay(peerId, this, bus );
    }

    /**
     * We're simply posting the event to the internal bus
     */
    @Override
    protected InternalEventTracker handleEvent(Event event) {
	InternalEventTracker eventTracker = bus.postEvent(event);
	if(eventTracker.hasErrors()) {
	    eventTracker.getFirstError().printStackTrace();
	}
	return eventTracker;
    }
    
    @Override
    protected void handleRemoteEvent(PeerEventContext pc) {
	handleEvent(pc.getEvent());
    }
    

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#forwardToAll(net.segoia.event.eventbus.Event)
     */
    @Override
    protected void forwardToAll(Event event) {
	/* 
	 * Because we want this event to get also to all the listeners on the internal bus, we simply post it there,
	 * and it will get to the peers to since all the relays are listening on the bus as well 
	 */
	handleEvent(event);
    }

    @Override
    protected void init() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void cleanUp() {
	
    }

    
}
