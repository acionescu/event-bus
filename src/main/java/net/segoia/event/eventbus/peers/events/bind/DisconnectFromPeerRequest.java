package net.segoia.event.eventbus.peers.events.bind;

import net.segoia.event.eventbus.peers.EventTransceiver;

public class DisconnectFromPeerRequest {
    private String peerId;
    private EventTransceiver transceiver;

    public DisconnectFromPeerRequest(EventTransceiver transceiver) {
	super();
	this.transceiver = transceiver;
    }

    public DisconnectFromPeerRequest(String peerId) {
	super();
	this.peerId = peerId;
    }

    public String getPeerId() {
	return peerId;
    }

    public void setPeerId(String peerId) {
	this.peerId = peerId;
    }

    public EventTransceiver getTransceiver() {
	return transceiver;
    }

    public void setTransceiver(EventTransceiver transceiver) {
	this.transceiver = transceiver;
    }

}
