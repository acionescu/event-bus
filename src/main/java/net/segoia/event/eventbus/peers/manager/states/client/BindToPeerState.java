package net.segoia.event.eventbus.peers.manager.states.client;

import net.segoia.event.eventbus.peers.PeerEventContext;
import net.segoia.event.eventbus.peers.PeerManager;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequest;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequestEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAccepted;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAcceptedEvent;
import net.segoia.event.eventbus.peers.manager.states.PeerState;

public class BindToPeerState extends PeerState {

    @Override
    public void onEnterState(PeerManager peerManager) {
	peerManager.getPeerContext().getRelay().start();

    }

    @Override
    public void onExitState(PeerManager peerManager) {
	// TODO Auto-generated method stub

    }

    @Override
    protected void registerLocalEventHandlers() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void registerPeerEventHandlers() {
	registerPeerEventProcessor(PeerBindAcceptedEvent.class, (c) -> {
	    handlePeerBindAccepted(c);
	});
    }
    
    protected void handlePeerBindAccepted(PeerEventContext<PeerBindAcceptedEvent> c) {
	PeerBindAcceptedEvent event = c.getEvent();
	PeerBindAccepted resp = event.getData();

	String localPeerId = event.getLastRelay();

	PeerManager peerManager = c.getPeerManager();

	if (peerManager == null) {
	    throw new RuntimeException("No peer manager found for localPeerId " + localPeerId);
	}

	peerManager.handlePeerBindAccepted(resp);

	PeerAuthRequest peerAuthRequest = new PeerAuthRequest(c.getNodeContext().getNodeInfo());

	peerManager.goToState(PeerManager.AUTH_TO_PEER);
	peerManager.forwardToPeer(new PeerAuthRequestEvent(peerAuthRequest));
    }

}
