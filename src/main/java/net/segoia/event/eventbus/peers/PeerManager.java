package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.comm.CommProtocolEventTransceiver;
import net.segoia.event.eventbus.peers.events.NodeInfo;
import net.segoia.event.eventbus.peers.events.PeerAcceptedEvent;
import net.segoia.event.eventbus.peers.events.PeerInfo;
import net.segoia.event.eventbus.peers.events.PeerLeavingEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequest;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAccepted;
import net.segoia.event.eventbus.peers.manager.states.PeerState;
import net.segoia.event.eventbus.peers.manager.states.client.AcceptedByPeerState;
import net.segoia.event.eventbus.peers.manager.states.client.AuthToPeerState;
import net.segoia.event.eventbus.peers.manager.states.client.BindToPeerState;
import net.segoia.event.eventbus.peers.manager.states.client.ConfirmProtocolToPeerState;
import net.segoia.event.eventbus.peers.manager.states.server.PeerAcceptedState;
import net.segoia.event.eventbus.peers.manager.states.server.PeerAuthAcceptedState;
import net.segoia.event.eventbus.peers.manager.states.server.PeerBindAcceptedState;
import net.segoia.event.eventbus.peers.manager.states.server.PeerBindRequestedState;
import net.segoia.event.eventbus.util.EBus;

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
    
    public void onProtocolConfirmed() {
	
	/* chain a protocol enforcing event transceiver */
	EventRelay relay = peerContext.getRelay();
	CommProtocolEventTransceiver commProtocolEventTransceiver = new CommProtocolEventTransceiver(relay.getTransceiver(), peerContext);
	relay.bind(commProtocolEventTransceiver);
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
