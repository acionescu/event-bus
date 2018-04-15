package net.segoia.event.eventbus.peers.events.bind;

import net.segoia.event.eventbus.peers.EventTransceiver;

public class ConnectToPeerRequest {
    private transient EventTransceiver transceiver;

    public ConnectToPeerRequest(EventTransceiver transceiver) {
	super();
	this.transceiver = transceiver;
    }

    public EventTransceiver getTransceiver() {
	return transceiver;
    }

    public void setTransceiver(EventTransceiver transceiver) {
	this.transceiver = transceiver;
    }

}
