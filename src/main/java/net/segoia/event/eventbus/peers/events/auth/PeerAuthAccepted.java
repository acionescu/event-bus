package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;

public class PeerAuthAccepted {
    private CommunicationProtocol communicationProtocol;

    public CommunicationProtocol getCommunicationProtocol() {
	return communicationProtocol;
    }

    public void setCommunicationProtocol(CommunicationProtocol communicationProtocol) {
	this.communicationProtocol = communicationProtocol;
    }

}
