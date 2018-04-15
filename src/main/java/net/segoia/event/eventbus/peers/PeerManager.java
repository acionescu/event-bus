package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.comm.CommProtocolEventTransceiver;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocolConfig;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocolDefinition;
import net.segoia.event.eventbus.peers.comm.NodeCommunicationStrategy;
import net.segoia.event.eventbus.peers.comm.PeerCommManager;
import net.segoia.event.eventbus.peers.events.NodeInfo;
import net.segoia.event.eventbus.peers.events.PeerAcceptedEvent;
import net.segoia.event.eventbus.peers.events.PeerInfo;
import net.segoia.event.eventbus.peers.events.PeerLeavingEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequest;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAccepted;
import net.segoia.event.eventbus.peers.events.session.PeerSessionStartedEvent;
import net.segoia.event.eventbus.peers.events.session.SessionInfo;
import net.segoia.event.eventbus.peers.events.session.SessionKey;
import net.segoia.event.eventbus.peers.events.session.SessionKeyData;
import net.segoia.event.eventbus.peers.events.session.SessionStartedData;
import net.segoia.event.eventbus.peers.exceptions.PeerSessionException;
import net.segoia.event.eventbus.peers.manager.states.PeerState;
import net.segoia.event.eventbus.peers.manager.states.client.AcceptedByPeerState;
import net.segoia.event.eventbus.peers.manager.states.client.AuthToPeerState;
import net.segoia.event.eventbus.peers.manager.states.client.BindToPeerState;
import net.segoia.event.eventbus.peers.manager.states.client.ConfirmProtocolToPeerState;
import net.segoia.event.eventbus.peers.manager.states.server.PeerAcceptedState;
import net.segoia.event.eventbus.peers.manager.states.server.PeerAuthAcceptedState;
import net.segoia.event.eventbus.peers.manager.states.server.PeerBindAcceptedState;
import net.segoia.event.eventbus.peers.manager.states.server.PeerBindRequestedState;
import net.segoia.event.eventbus.peers.security.CommDataContext;
import net.segoia.event.eventbus.peers.security.CommManager;
import net.segoia.event.eventbus.peers.security.CommOperationException;
import net.segoia.event.eventbus.peers.security.EventNodeSecurityManager;
import net.segoia.event.eventbus.peers.security.OperationData;
import net.segoia.event.eventbus.peers.security.PeerCommContext;
import net.segoia.event.eventbus.peers.security.SessionKeyOutgoingAccumulator;
import net.segoia.event.eventbus.peers.security.SignCommOperationOutput;
import net.segoia.event.eventbus.util.EBus;
import net.segoia.util.crypto.CryptoUtil;

/**
 * Implements a certain communication policy with a peer over an {@link EventRelay}
 * 
 * @author adi
 *
 */
public class PeerManager implements PeerEventListener {
    private PeerContext peerContext;
    private String peerId;
    private String peerType;

    private PeerState state;

    /**
     * The state in which the communication with the peer is established and can implement whatever app logic is needed
     */
    private PeerState acceptedState;

    /* when functioning as client, states */
    public static PeerState BIND_TO_PEER = new BindToPeerState();
    public static PeerState AUTH_TO_PEER = new AuthToPeerState();
    public static PeerState CONFIRM_PROTOCOL_TO_PEER = new ConfirmProtocolToPeerState();
    public static PeerState ACCEPTED_BY_PEER = new AcceptedByPeerState();

    /* when functioning as server, states */
    public static PeerState PEER_BIND_REQUESTED = new PeerBindRequestedState();
    public static PeerState PEER_BIND_ACCEPTED = new PeerBindAcceptedState();
    public static PeerState PEER_AUTH_ACCEPTED = new PeerAuthAcceptedState();
    public static PeerState PEER_ACCEPTED = new PeerAcceptedState();

    public PeerManager(String peerId, EventTransceiver transceiver) {

	peerContext = new PeerContext(peerId, transceiver);

	DefaultEventRelay relay = new DefaultEventRelay(peerId, transceiver);
	/* listen on events from peer */
	relay.setRemoteEventListener(this);
	peerContext.setRelay(relay);
	this.peerType = transceiver.getClass().getSimpleName();
	this.peerId = peerId;
	/* bind relay to transceiver */
	relay.bind();
    }

    public void goToState(PeerState newState) {
	if (state != null) {
	    state.onExitState(this);
	}
	state = newState;
	state.onEnterState(this);
    }

    public String getPeerId() {
	return peerContext.getPeerId();
    }

    public void setPeerInfo(NodeInfo peerInfo) {
	peerContext.setPeerInfo(peerInfo);
    }

    public NodeInfo getPeerInfo() {
	return peerContext.getPeerInfo();
    }

    public void start() {
	/* the peer is the server, we are the client, so we need to initiate connection */
	if (peerContext.isInServerMode()) {
	    if (acceptedState == null) {
		acceptedState = ACCEPTED_BY_PEER;
	    }
	    goToState(BIND_TO_PEER);

	} else {
	    /* we are the server and a client requested to bind to us */
	    if (acceptedState == null) {
		acceptedState = PEER_ACCEPTED;
	    }

	    goToState(PEER_BIND_REQUESTED);

	}
    }

    public void terminate() {

    }

    protected void cleanUp() {

    }

    public void setUpSessionCommManager() {
	PeerCommManager peerCommManager = new PeerCommManager();

	peerContext.setPeerCommManager(peerCommManager);

	/* get the session communication manager */
	EventNodeSecurityManager securityManager = getNodeContext().getSecurityManager();

	PeerCommContext peerCommContext = buildPeerCommContext();
	peerContext.setPeerCommContext(peerCommContext);

	/*
	 * Get the session comm manager
	 */
	CommManager sessionCommManager = securityManager.getSessionCommManager(peerCommContext);

	peerCommManager.setSessionCommManager(sessionCommManager);
    }

    public void generateNewSession() {
	EventNodeSecurityManager securityManager = getNodeContext().getSecurityManager();

	try {
	    securityManager.generateNewSessionKey(peerContext);

	} catch (PeerSessionException e) {
	    handleError(e);

	}
    }

    public void setUpDirectCommManager() {
	EventNodeSecurityManager securityManager = getNodeContext().getSecurityManager();
	PeerCommContext peerCommContext = peerContext.getPeerCommContext();
	PeerCommManager peerCommManager = peerContext.getPeerCommManager();

	/* now we can build a direct comm manager */
	CommManager directCommManager = securityManager.getDirectCommManager(peerCommContext);

	peerCommManager.setDirectCommManager(directCommManager);
    }

    public void onProtocolConfirmed() {
	setUpSessionCommManager();
	generateNewSession();
	setUpDirectCommManager();
	
	/* send the session */
	startNewPeerSession();
	
	/* activate the transceiver implementing the protocol */
	setUpCommProtocolTransceiver();

    }

    public void startNewPeerSession() {
	SessionKey sessionKey = peerContext.getSessionKey();

	PeerCommManager peerCommManager = peerContext.getPeerCommManager();

	SessionInfo sessionInfo = null;

	try {
	    SessionKeyOutgoingAccumulator opAcc = new SessionKeyOutgoingAccumulator(new OperationData(sessionKey.getKeyBytes()));
	    
	    /* prepare session token */
	    CommDataContext processedSessionData = peerCommManager.getSessionCommManager()
		    .processsOutgoingData(new CommDataContext(sessionKey.getKeyBytes()));
	    
	    SignCommOperationOutput out = (SignCommOperationOutput)processedSessionData.getResult();
	    
	    /* encode base64 */
	    String sessionToken = CryptoUtil.base64Encode(out.getData());
	    String sessionTokenSignature = CryptoUtil.base64Encode(out.getSignature());
	    
	    //TODO: send the iv as well

	    SessionKeyData sessionKeyData = new SessionKeyData(sessionToken, sessionTokenSignature, sessionKey.getKeyDef());
	    
	    sessionInfo = new SessionInfo(sessionKey.getSessionId(), sessionKeyData);

	} catch (CommOperationException e) {
	    handleError(e);
	    return;
	}

	/* now we can send the session start event */

	forwardToPeer(new PeerSessionStartedEvent(new SessionStartedData(sessionInfo)));
    }
    
    public void setUpCommProtocolTransceiver() {
	/* chain a protocol enforcing event transceiver */
	EventRelay relay = peerContext.getRelay();
	CommProtocolEventTransceiver commProtocolEventTransceiver = new CommProtocolEventTransceiver(
		relay.getTransceiver(), peerContext);
	relay.bind(commProtocolEventTransceiver);
    }

    public void handleError(Exception e) {
	e.printStackTrace();
    }

    protected PeerCommContext buildPeerCommContext() {
	CommunicationProtocol commProtocol = peerContext.getCommProtocol();
	CommunicationProtocolDefinition protocolDefinition = commProtocol.getProtocolDefinition();
	NodeCommunicationStrategy clientCommStrategy = protocolDefinition.getClientCommStrategy();
	NodeCommunicationStrategy serverCommStrategy = protocolDefinition.getServerCommStrategy();

	CommunicationProtocolConfig protocolConfig = commProtocol.getConfig();

	NodeCommunicationStrategy txStrategy = null;
	NodeCommunicationStrategy rxStrategy = null;
	int ourIdentityIndex;
	int peerIdentityIndex;

	if (peerContext.isInServerMode()) {
	    /* we're acting as client */
	    txStrategy = clientCommStrategy;
	    rxStrategy = serverCommStrategy;

	    ourIdentityIndex = protocolConfig.getClientNodeIdentity();
	    peerIdentityIndex = protocolConfig.getServerNodeIdentity();
	} else {
	    /* we're acting as server */
	    txStrategy = serverCommStrategy;
	    rxStrategy = clientCommStrategy;

	    peerIdentityIndex = protocolConfig.getClientNodeIdentity();
	    ourIdentityIndex = protocolConfig.getServerNodeIdentity();
	}

	return new PeerCommContext(ourIdentityIndex, peerIdentityIndex, txStrategy, rxStrategy, peerContext);
    }

    public void onReady() {
	goToState(acceptedState);
	postEvent(new PeerAcceptedEvent(new PeerInfo(peerId, peerType, peerContext.getPeerInfo())));
    }

    protected void postEvent(Event event) {
	peerContext.getNodeContext().postEvent(event);
    }

    public void handlePeerBindAccepted(PeerBindAccepted data) {
	peerContext.setPeerInfo(data.getNodeInfo());
    }

    public void handlePeerAuthRequest(PeerAuthRequest data) {
	peerContext.setPeerInfo(data.getNodeInfo());
    }

    public void handleEventFromPeer(Event event) {
	state.handleEventFromPeer(new PeerEventContext(event, this));
    }

    public void forwardToPeer(Event event) {
	peerContext.getRelay().sendEvent(event);
    }

    public void setInServerMode(boolean inServerMode) {
	peerContext.setInServerMode(inServerMode);
    }

    public PeerContext getPeerContext() {
	return peerContext;
    }

    public boolean isRemoteAgent() {
	return peerContext.isRemoteAgent();
    }

    @Override
    public void onPeerEvent(Event event) {
	EBus.postEvent(event);
	handleEventFromPeer(event);
    }

    public EventNodeContext getNodeContext() {
	return peerContext.getNodeContext();
    }

    @Override
    public void onPeerLeaving() {
	postEvent(new PeerLeavingEvent(new PeerInfo(peerId, peerType, peerContext.getPeerInfo())));
    }

}
