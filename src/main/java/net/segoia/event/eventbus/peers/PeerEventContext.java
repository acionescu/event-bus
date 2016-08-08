package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.CustomEventContext;
import net.segoia.event.eventbus.Event;

public class PeerEventContext extends CustomEventContext<Event>{
    private EventRelay relay;
    
    public PeerEventContext(EventRelay relay, Event event) {
	super(event,null);
	this.relay = relay;
    }

    /**
     * @return the relay
     */
    public EventRelay getRelay() {
        return relay;
    }


    
}
