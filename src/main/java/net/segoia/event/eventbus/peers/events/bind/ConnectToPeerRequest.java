package net.segoia.event.eventbus.peers.events.bind;

import java.util.List;

import net.segoia.event.eventbus.peers.EventTransceiver;
import net.segoia.event.eventbus.peers.security.PrivateIdentityData;

public class ConnectToPeerRequest {
    private transient EventTransceiver transceiver;
    private transient List<PrivateIdentityData<?>> ourIdentities;

    public ConnectToPeerRequest(EventTransceiver transceiver) {
	super();
	this.transceiver = transceiver;
    }

    public ConnectToPeerRequest(EventTransceiver transceiver, List<PrivateIdentityData<?>> ourIdentities) {
	super();
	this.transceiver = transceiver;
	this.ourIdentities = ourIdentities;
    }

    public EventTransceiver getTransceiver() {
	return transceiver;
    }

    public void setTransceiver(EventTransceiver transceiver) {
	this.transceiver = transceiver;
    }

    public List<PrivateIdentityData<?>> getOurIdentities() {
	return ourIdentities;
    }

    public void setOurIdentities(List<PrivateIdentityData<?>> ourIdentities) {
	this.ourIdentities = ourIdentities;
    }

}
