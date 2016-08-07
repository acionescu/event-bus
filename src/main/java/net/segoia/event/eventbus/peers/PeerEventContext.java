package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;

public class PeerEventContext extends EventContext{
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
