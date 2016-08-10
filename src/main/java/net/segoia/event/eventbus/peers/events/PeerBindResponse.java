package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.peers.EventRelay;
import net.segoia.event.eventbus.peers.PeeringRequest;

public class PeerBindResponse {
    private EventRelay relay;
    private PeeringRequest bindRequest;

    public PeerBindResponse(EventRelay relay, PeeringRequest bindRequest) {
	super();
	this.relay = relay;
	this.bindRequest = bindRequest;
    }



    /**
     * @return the relay
     */
    public EventRelay getRelay() {
        return relay;
    }



    /**
     * @return the bindRequest
     */
    public PeeringRequest getBindRequest() {
        return bindRequest;
    }
    
    

}
