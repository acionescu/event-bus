package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.TrueCondition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
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
    
    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#registerHandlers()
     */
    @Override
    protected void registerHandlers() {
	super.registerHandlers();
	/* register a generic handler that will post all events to the bus */
	addEventHandler((c) -> this.handleEvent(c.getEvent()));
    }
    
    

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#handleRemoteEvent(net.segoia.event.eventbus.EventContext)
     */
    @Override
    protected boolean handleRemoteEvent(EventContext pc) {
	/* we want to peek on all events */
	handleEvent(pc.getEvent());
	return super.handleRemoteEvent(pc);
    }
    
    

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#forwardToAll(net.segoia.event.eventbus.Event)
     */
    @Override
    protected void forwardToAll(Event event) {
	if(event.from() == null) {
	    /* if it comes from us, peek */
	    handleEvent(event);
	}
	super.forwardToAll(event);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#forwardTo(net.segoia.event.eventbus.Event, java.lang.String)
     */
    @Override
    protected void forwardTo(Event event, String to) {
	if(event.from() == null) {
	    /* if it comes from us, peek */
	    handleEvent(event);
	}
	super.forwardTo(event, to);
    }

    /**
     * We're simply posting the event to the provided bus
     */
    protected InternalEventTracker handleEvent(Event event) {
	InternalEventTracker eventTracker = bus.postEvent(event);
	if(eventTracker.hasErrors()) {
	    eventTracker.getFirstError().printStackTrace();
	}
	return eventTracker;
    }
    
    
    protected void setRequestedEventsCondition() {
   	config.setDefaultRequestedEvents(new TrueCondition());
       }

       @Override
       protected EventRelay buildLocalRelay(String peerId) {
   	return new DefaultEventRelay(peerId, this);
       }

    
    @Override
    public void cleanUp() {
	
    }

    @Override
    protected void onTerminate() {
	// TODO Auto-generated method stub
	
    }

    @Override
    protected void nodeInit() {
	// TODO Auto-generated method stub
	
    }

    @Override
    protected void nodeConfig() {
	// TODO Auto-generated method stub
	
    }

    
    
}
