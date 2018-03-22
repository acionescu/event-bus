package net.segoia.event.eventbus.peers.events.auth;

import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.events.NodeInfo;

public class PeerAuthRequest {
    private NodeInfo nodeInfo;
    /**
     * Can optionally propose a communication protocol
     */
    private CommunicationProtocol communicationProtocol;

    public PeerAuthRequest(NodeInfo nodeInfo) {
	super();
	this.nodeInfo = nodeInfo;

    }

    public PeerAuthRequest() {
	super();
    }

    public NodeInfo getNodeInfo() {
	return nodeInfo;
    }

    public void setNodeInfo(NodeInfo nodeInfo) {
	this.nodeInfo = nodeInfo;
    }

    public CommunicationProtocol getCommunicationProtocol() {
	return communicationProtocol;
    }

    public void setCommunicationProtocol(CommunicationProtocol communicationProtocol) {
	this.communicationProtocol = communicationProtocol;
    }

}
