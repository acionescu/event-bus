package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.events.NodeInfo;
import net.segoia.event.eventbus.peers.events.SessionInfo;
import net.segoia.event.eventbus.peers.security.PublicIdentityManager;

public class PeerContext {
    private String peerId;

    /**
     * The relay through which the current node communicates with the peer node
     */
    private EventTransceiver transceiver;

    private EventRelay relay;

    private NodeInfo peerInfo;

    /**
     * If set true, then this peer acts as server, and we are a client
     */
    private boolean inServerMode;

    /**
     * The protocol used to communicate with the peer
     */
    private CommunicationProtocol commProtocol;

    private SessionInfo sessionInfo;

    private boolean remoteAgent;

    private EventNodeContext nodeContext;

    /**
     * A public identity manger for this peer
     */
    private PublicIdentityManager peerIdentityManager;

    public PeerContext(String peerId, EventTransceiver transceiver) {
	super();
	this.peerId = peerId;
	this.transceiver = transceiver;
    }

    public EventRelay getRelay() {
	return relay;
    }

    public void setRelay(EventRelay relay) {
	this.relay = relay;
    }

    public NodeInfo getPeerInfo() {
	return peerInfo;
    }

    public void setPeerInfo(NodeInfo peerInfo) {
	this.peerInfo = peerInfo;
    }

    public String getPeerId() {
	return peerId;
    }

    public String getCommunicationChannel() {
	return relay.getChannel();
    }

    public boolean isInServerMode() {
	return inServerMode;
    }

    public void setInServerMode(boolean inServerMode) {
	this.inServerMode = inServerMode;
    }

    public CommunicationProtocol getCommProtocol() {
	return commProtocol;
    }

    public void setCommProtocol(CommunicationProtocol commProtocol) {
	this.commProtocol = commProtocol;
    }

    public SessionInfo getSessionInfo() {
	return sessionInfo;
    }

    public void setSessionInfo(SessionInfo sessionInfo) {
	this.sessionInfo = sessionInfo;
    }

    public boolean isRemoteAgent() {
	return remoteAgent;
    }

    public void setRemoteAgent(boolean remoteAgent) {
	this.remoteAgent = remoteAgent;
    }

    public EventNodeContext getNodeContext() {
	return nodeContext;
    }

    public void setNodeContext(EventNodeContext nodeContext) {
	this.nodeContext = nodeContext;
    }

    public PublicIdentityManager getPeerIdentityManager() {
	return peerIdentityManager;
    }

    public void setPeerIdentityManager(PublicIdentityManager peerIdentityManager) {
	this.peerIdentityManager = peerIdentityManager;
    }

}
