package net.segoia.event.eventbus.peers.manager.states.server;

import net.segoia.event.eventbus.peers.PeerEventContext;
import net.segoia.event.eventbus.peers.PeerManager;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.events.auth.PeerProtocolConfirmedEvent;
import net.segoia.event.eventbus.peers.events.auth.ProtocolConfirmation;
import net.segoia.event.eventbus.peers.events.session.PeerSessionStartedEvent;
import net.segoia.event.eventbus.peers.events.session.SessionStartedData;
import net.segoia.event.eventbus.peers.manager.states.PeerState;

public class PeerAuthAcceptedState extends PeerState {

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
	registerPeerEventProcessor(PeerProtocolConfirmedEvent.class, (c) -> {
	    handleProtocolConfirmed(c);
	});

    }

    protected void handleProtocolConfirmed(PeerEventContext<PeerProtocolConfirmedEvent> c) {
	PeerProtocolConfirmedEvent event = c.getEvent();
	PeerManager peerManager = c.getPeerManager();

	ProtocolConfirmation data = event.getData();

	/* check again if the protocols match */
	CommunicationProtocol ourProtocol = peerManager.getPeerContext().getCommProtocol();
	CommunicationProtocol peerProtocol = data.getProtocol();

	if (ourProtocol.equals(peerProtocol)) {
	    /* if they match, initiate the session */

	    SessionStartedData sessionStartedData = new SessionStartedData(
		    peerManager.getNodeContext().generateNewSession());
	    peerManager.onReady();
	    
	    
	    
	    peerManager.forwardToPeer(new PeerSessionStartedEvent(sessionStartedData));
	}

	else {
	    /* ups, they don't match */

	    // TODO: handle this
	}

    }

}
