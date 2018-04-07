package net.segoia.event.eventbus.peers.manager.states.client;

import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.PeerEventContext;
import net.segoia.event.eventbus.peers.PeerManager;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthAccepted;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthAcceptedEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerProtocolConfirmedEvent;
import net.segoia.event.eventbus.peers.events.auth.ProtocolConfirmation;
import net.segoia.event.eventbus.peers.events.session.PeerSessionStartedEvent;
import net.segoia.event.eventbus.peers.events.session.SessionStartedData;
import net.segoia.event.eventbus.peers.exceptions.PeerAuthRequestRejectedException;
import net.segoia.event.eventbus.peers.exceptions.PeerCommunicationNegotiationFailedException;
import net.segoia.event.eventbus.peers.manager.states.PeerState;

public class AuthToPeerState extends PeerState{

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
	registerPeerEventProcessor(PeerAuthAcceptedEvent.class, (c)->{
	    handlePeerAuthAccepted(c);
	});
	
    }
    
   
    protected void handlePeerAuthAccepted(PeerEventContext<PeerAuthAcceptedEvent> c) {
	PeerAuthAcceptedEvent event = c.getEvent();
	PeerManager peerManager = c.getPeerManager();
	PeerAuthAccepted data = event.getData();

	CommunicationProtocol protocolFromPeer = data.getCommunicationProtocol();

	PeerContext peerContext = peerManager.getPeerContext();
	CommunicationProtocol ourProtocol = peerContext.getCommProtocol();

	if (ourProtocol == null) {
	    /* if we hanve't proposed a protocol, see if we can find a matching supported protocol */
	    try {
		ourProtocol = peerManager.getNodeContext().getSecurityManager().establishPeerCommunicationProtocol(peerContext);
	    } catch (PeerCommunicationNegotiationFailedException ex) {
		ex.printStackTrace();
	    } catch (PeerAuthRequestRejectedException arex) {
		arex.printStackTrace();
	    }

	    /* set the protocol on peer context */
	    peerContext.setCommProtocol(ourProtocol);
	}

	/* check that the two protocols match */

	
	if (ourProtocol.equals(protocolFromPeer)) {
	    /* yey, we have a matching protocol, send confirmation */
	    ProtocolConfirmation protocolConfirmation = new ProtocolConfirmation(ourProtocol);
	    
	    peerManager.goToState(PeerManager.CONFIRM_PROTOCOL_TO_PEER);
	    peerManager.forwardToPeer(new PeerProtocolConfirmedEvent(protocolConfirmation));
	    
	    /* after we send protocol confirmation event start using it */
	    peerManager.onProtocolConfirmed();
	}
	else {
	    /* protocols don't match */
	    System.out.println("PROTOCOLS don't match");
	}
    }

}
