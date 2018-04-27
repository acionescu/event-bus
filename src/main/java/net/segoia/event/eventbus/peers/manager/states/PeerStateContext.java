package net.segoia.event.eventbus.peers.manager.states;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.PeerContext;

public class PeerStateContext {
    private PeerManagerState state;
    private PeerContext peerContext;
    private Event event;

    public PeerStateContext(PeerManagerState state, PeerContext peerContext, Event event) {
	super();
	this.state = state;
	this.peerContext = peerContext;
	this.event = event;
    }

    public PeerManagerState getState() {
	return state;
    }

    public void setState(PeerManagerState state) {
	this.state = state;
    }

    public PeerContext getPeerContext() {
	return peerContext;
    }

    public void setPeerContext(PeerContext peerContext) {
	this.peerContext = peerContext;
    }

    public Event getEvent() {
	return event;
    }

    public void setEvent(Event event) {
	this.event = event;
    }

}
