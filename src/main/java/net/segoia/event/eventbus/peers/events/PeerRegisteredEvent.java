package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.events.PeerRegisteredEvent.Data;

@EventType("PEER:RESPONSE:REGISTERED")
public class PeerRegisteredEvent extends CustomEvent<Data>{
    
    public PeerRegisteredEvent(String peerId) {
	super(PeerRegisteredEvent.class);
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("Data [");
	    if (peerId != null)
		builder.append("peerId=").append(peerId);
	    builder.append("]");
	    return builder.toString();
	}
	
	
    }

}
