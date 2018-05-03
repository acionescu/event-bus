package net.segoia.event.eventbus.peers.manager.states.server;

import net.segoia.event.eventbus.peers.PeerEventContext;
import net.segoia.event.eventbus.peers.PeerManager;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthAcceptedEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRejectedEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequestEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAcceptedEvent;
import net.segoia.event.eventbus.peers.exceptions.PeerAuthRequestRejectedException;
import net.segoia.event.eventbus.peers.exceptions.PeerCommunicationNegotiationFailedException;
import net.segoia.event.eventbus.peers.manager.states.PeerManagerState;
import net.segoia.event.eventbus.peers.vo.RequestRejectReason;
import net.segoia.event.eventbus.peers.vo.auth.MessageAuthRejectedReason;
import net.segoia.event.eventbus.peers.vo.auth.PeerAuthAccepted;
import net.segoia.event.eventbus.peers.vo.auth.PeerAuthRejected;
import net.segoia.event.eventbus.peers.vo.auth.PeerAuthRequest;
import net.segoia.event.eventbus.peers.vo.bind.PeerBindAccepted;
import net.segoia.event.eventbus.peers.vo.comm.CommunicationProtocol;

public class PeerBindAcceptedState extends PeerManagerState{

    @Override
    public void onEnterState(PeerManager peerManager) {
	
	PeerBindAccepted resp = new PeerBindAccepted(peerManager.getNodeContext().getNodeInfo());

	PeerBindAcceptedEvent respEvent = new PeerBindAcceptedEvent(resp);

//	respEvent.to(peerManager.getPeerId());

	peerManager.forwardToPeer(respEvent);
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
	/* wait for auth event */
	registerPeerEventProcessor(PeerAuthRequestEvent.class, (c)->{
	    handlePeerAuthRequest(c);
	});
	
	
    }
    
    protected void handlePeerAuthRequest(PeerEventContext<PeerAuthRequestEvent> c) {
	PeerAuthRequestEvent event = c.getEvent();
	PeerAuthRequest data = event.getData();

	String localPeerId = event.getLastRelay();

	PeerManager peerManager = c.getPeerManager();

	if (peerManager == null) {
	    throw new RuntimeException("No peer manager found for localPeerId " + localPeerId);
	}

	peerManager.handlePeerAuthRequest(data);

	try {
	    CommunicationProtocol commProtocol = peerManager.getNodeContext().getSecurityManager()
		    .establishPeerCommunicationProtocol(peerManager.getPeerContext());
	    CommunicationProtocol protocolProposedByPeer = data.getCommunicationProtocol();

	    /* set the protocol we found on the peer context */
	    peerManager.getPeerContext().setCommProtocol(commProtocol);
	    if (protocolProposedByPeer != null) {
		if (commProtocol.equals(protocolProposedByPeer)) {
		    /* The protocol chose by us matches with the one proposed by peer. All is good */

		    // TODO: send auth accepted event
		    acceptPeerAuth(peerManager);
		} else {
		    /* Ups, the proposed protocol doesn't match with the one we found */

		    /* Fail, for now */

		    rejectPeerAuth(peerManager, new RequestRejectReason<CommunicationProtocol>(
			    "Don't agree on protocol. Found a better one.", commProtocol));
		    // TODO: Propose the one we found

//		    acceptPeerAuth(peerManager);
		}
	    } else {
		// TODO: propose the protocol we found

		acceptPeerAuth(peerManager);
	    }

	} catch (PeerCommunicationNegotiationFailedException ex) {
	    rejectPeerAuth(peerManager, new MessageAuthRejectedReason(ex.getMessage()));
	} catch (PeerAuthRequestRejectedException arex) {
	    rejectPeerAuth(peerManager, arex.getAuthRejectedData().getReason());
	}
    }
    
    protected void acceptPeerAuth(PeerManager peerManager) {
	PeerAuthAccepted peerAuthAccepted = new PeerAuthAccepted();
	peerAuthAccepted.setCommunicationProtocol(peerManager.getPeerContext().getCommProtocol());
	peerManager.goToState(PeerManager.PEER_AUTH_ACCEPTED);
	peerManager.forwardToPeer(new PeerAuthAcceptedEvent(peerAuthAccepted));
    }

    protected void rejectPeerAuth(PeerManager peerManager, RequestRejectReason reason) {
	PeerAuthRejected peerAuthRejected = new PeerAuthRejected(reason);
	peerManager.forwardToPeer(new PeerAuthRejectedEvent(peerAuthRejected));
    }

}
