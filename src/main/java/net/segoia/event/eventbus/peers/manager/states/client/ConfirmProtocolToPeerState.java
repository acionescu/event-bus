package net.segoia.event.eventbus.peers.manager.states.client;

import net.segoia.event.eventbus.peers.PeerEventContext;
import net.segoia.event.eventbus.peers.PeerManager;
import net.segoia.event.eventbus.peers.events.session.PeerSessionStartedEvent;
import net.segoia.event.eventbus.peers.events.session.SessionStartedData;
import net.segoia.event.eventbus.peers.manager.states.PeerState;

public class ConfirmProtocolToPeerState extends PeerState {

    @Override
    public void onEnterState(PeerManager peerManager) {
	// TODO Auto-generated method stub

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

	peerManager.getPeerContext().setSessionInfo(data.getSessionInfo());

	peerManager.onReady();
    }

}
