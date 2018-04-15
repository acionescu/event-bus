package net.segoia.event.eventbus.peers.manager.states.client;

import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.PeerEventContext;
import net.segoia.event.eventbus.peers.PeerManager;
import net.segoia.event.eventbus.peers.comm.PeerCommManager;
import net.segoia.event.eventbus.peers.events.session.PeerSessionStartedEvent;
import net.segoia.event.eventbus.peers.events.session.SessionInfo;
import net.segoia.event.eventbus.peers.events.session.SessionKeyData;
import net.segoia.event.eventbus.peers.events.session.SessionStartedData;
import net.segoia.event.eventbus.peers.manager.states.PeerState;
import net.segoia.event.eventbus.peers.security.CommOperationException;
import net.segoia.event.eventbus.peers.security.EventNodeSecurityManager;
import net.segoia.util.crypto.CryptoUtil;

public class ConfirmProtocolToPeerState extends PeerState {

    @Override
    public void onEnterState(PeerManager peerManager) {

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
	registerPeerEventProcessor(PeerSessionStartedEvent.class, (c) -> {
	    handleSessionStarted(c);
	});
    }

    protected void handleSessionStarted(PeerEventContext<PeerSessionStartedEvent> c) {
	PeerSessionStartedEvent event = c.getEvent();
	PeerManager peerManager = c.getPeerManager();

	SessionStartedData data = event.getData();

	SessionInfo sessionInfo = data.getSessionInfo();

//	peerManager.setUpSessionCommManager();

	PeerContext peerContext = peerManager.getPeerContext();
	EventNodeSecurityManager securityManager = peerContext.getNodeContext().getSecurityManager();
	try {

	    securityManager.buildSessionFromSessionInfo(peerContext, sessionInfo);
	} catch (CommOperationException e) {
	    peerManager.handleError(e);
	    return;
	}
	peerManager.setUpDirectCommManager();
	peerManager.setUpCommProtocolTransceiver();
	peerManager.onReady();
    }

}
