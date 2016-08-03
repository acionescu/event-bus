package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

public class PeerEventContext {
    private EventRelay relay;
    private Event event;
    
    public PeerEventContext(EventRelay relay, Event event) {
	super();
	this.relay = relay;
	this.event = event;
    }

    /**
     * @return the relay
     */
    public EventRelay getRelay() {
        return relay;
    }



    /**
     * @return the event
     */
    public Event getEvent() {
        return event;
    }
    
}
