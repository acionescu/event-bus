package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventListener;
import net.segoia.event.eventbus.peers.events.NodeInfo;
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

/**
 * Implements a certain communication policy with a peer over an {@link EventRelay}
 * 
 * @author adi
 *
 */
public class PeerManager implements EventListener{
    private PeerContext peerContext;
    private String peerId;

    private PeerState state;

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

	this.peerId = peerId;
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
	    goToState(BIND_TO_PEER);
	} else {
	    /* we are the server and a client requested to bind to us */
	    goToState(PEER_BIND_REQUESTED);
	}
    }

    public void terminate() {

    }

    protected void cleanUp() {

    }

    public void onReady() {

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
    public void onEvent(Event event) {
	handleEventFromPeer(event);
    }
    
    public EventNodeContext getNodeContext() {
	return peerContext.getNodeContext();
    }

}
