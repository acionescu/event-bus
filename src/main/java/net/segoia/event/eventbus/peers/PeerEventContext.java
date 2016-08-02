package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

public class PeerEventContext {
    private EventBusRelay relay;
    private Event event;
    
    public PeerEventContext(EventBusRelay relay, Event event) {
	super();
	this.relay = relay;
	this.event = event;
    }

    /**
     * @return the relay
     */
    public EventBusRelay getRelay() {
        return relay;
    }



    /**
     * @return the event
     */
    public Event getEvent() {
        return event;
    }
    
}
