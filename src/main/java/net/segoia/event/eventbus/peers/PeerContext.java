package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.comm.PeerCommManager;
import net.segoia.event.eventbus.peers.events.NodeInfo;
import net.segoia.event.eventbus.peers.events.session.SessionKey;
import net.segoia.event.eventbus.peers.security.PeerCommContext;
import net.segoia.event.eventbus.peers.security.PrivateIdentityManager;
import net.segoia.event.eventbus.peers.security.PublicIdentityManager;
import net.segoia.event.eventbus.peers.security.SessionManager;

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

    private SessionKey sessionKey;

    private boolean remoteAgent;

    private EventNodeContext nodeContext;

    /**
     * A public identity manger for this peer
     */
    private PublicIdentityManager peerIdentityManager;

    private PrivateIdentityManager ourIdentityManager;

    private SessionManager sessionManager;

    private PeerCommManager peerCommManager;
    
    private PeerCommContext peerCommContext;

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

    public SessionKey getSessionKey() {
	return sessionKey;
    }

    public void setSessionKey(SessionKey sessionKey) {
	this.sessionKey = sessionKey;
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

    public PeerCommManager getPeerCommManager() {
	return peerCommManager;
    }

    public void setPeerCommManager(PeerCommManager peerCommManager) {
	this.peerCommManager = peerCommManager;
    }

    public SessionManager getSessionManager() {
	return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
	this.sessionManager = sessionManager;
    }

    public PrivateIdentityManager getOurIdentityManager() {
	return ourIdentityManager;
    }

    public void setOurIdentityManager(PrivateIdentityManager ourIdentityManager) {
	this.ourIdentityManager = ourIdentityManager;
    }

    public PeerCommContext getPeerCommContext() {
        return peerCommContext;
    }

    public void setPeerCommContext(PeerCommContext peerCommContext) {
        this.peerCommContext = peerCommContext;
    }

    public EventTransceiver getTransceiver() {
        return transceiver;
    }

    public void setTransceiver(EventTransceiver transceiver) {
        this.transceiver = transceiver;
    }

}
