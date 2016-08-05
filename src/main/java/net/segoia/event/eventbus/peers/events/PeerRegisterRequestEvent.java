package net.segoia.event.eventbus.peers.events;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.events.PeerRegisterRequestEvent.Data;

@EventType("PEER:REQUEST:REGISTER")
public class PeerRegisterRequestEvent extends CustomEvent<Data>{
    
    public PeerRegisterRequestEvent(String peerId, Condition eventsCondition) {
	super(PeerRegisterRequestEvent.class);
	this.data = new Data(peerId, eventsCondition);
    }

    public class Data{
	/**
	 * Id of the peer to be registered
	 */
	private String peerId;
	
	/**
	 * Only events respecting this condition should be forwarded
	 */
	private Condition eventsCondition;

	public Data(String peerId, Condition eventsCondition) {
	    super();
	    this.peerId = peerId;
	    this.eventsCondition = eventsCondition;
	}

	/**
	 * @return the peerId
	 */
	public String getPeerId() {
	    return peerId;
	}

	/**
	 * @return the eventsCondition
	 */
	public Condition getEventsCondition() {
	    return eventsCondition;
	}

	/**
	 * @param peerId the peerId to set
	 */
	public void setPeerId(String peerId) {
	    this.peerId = peerId;
	}

	/**
	 * @param eventsCondition the eventsCondition to set
	 */
	public void setEventsCondition(Condition eventsCondition) {
	    this.eventsCondition = eventsCondition;
	}
	
    }

}
