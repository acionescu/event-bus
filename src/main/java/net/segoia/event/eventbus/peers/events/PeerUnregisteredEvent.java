package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.events.PeerUnregisteredEvent.Data;

@EventType("PEER:RESPONSE:UNREGISTERED")
public class PeerUnregisteredEvent extends CustomEvent<Data>{
    
    public PeerUnregisteredEvent(String peerId) {
	super(PeerUnregisteredEvent.class);
	this.data = new Data(peerId);
    }

    public class Data{
	private String peerId;
	
	public Data(String peerId) {
	    super();
	    this.peerId = peerId;
	}

	/**
	 * @return the peerId
	 */
	public String getPeerId() {
	    return peerId;
	}

	/**
	 * @param peerId the peerId to set
	 */
	public void setPeerId(String peerId) {
	    this.peerId = peerId;
	}
	
    }

}
