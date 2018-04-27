package net.segoia.event.eventbus.peers.manager.states.server;

import java.util.ArrayList;

import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.PeerEventContext;
import net.segoia.event.eventbus.peers.PeerManager;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdIssuedData;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdIssuedEvent;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdRequest;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdRequestEvent;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdRequestRejectedReason;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdRequestRjectedEvent;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.exceptions.PeerRequestHandlingException;
import net.segoia.event.eventbus.peers.exceptions.PeerRequestRejectedException;
import net.segoia.event.eventbus.peers.manager.states.PeerManagerState;
import net.segoia.event.eventbus.peers.manager.states.PeerStateContext;
import net.segoia.event.eventbus.peers.security.EventNodeSecurityManager;
import net.segoia.event.eventbus.services.NodeIdentityProfile;

public class PeerAcceptedState extends PeerManagerState {
    public static final String ID = "PeerAcceptedState";

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
	registerPeerEventProcessor(ServiceAccessIdRequestEvent.class, (c) -> {
	    System.out.println("handling service access id request");
	    try {
		handleServiceAccessIdRequest(c);
	    } finally {
		c.getEvent().setHandled();
	    }
	});

	registerPeerEventProcessor((c) -> {
	    if (!c.getEvent().isHandled()) {
		System.out.println("posting to node " + c.getEvent().getEt());
		c.getPeerManager().postEvent(c.getEvent());
	    }
	});

    }

    protected void handleServiceAccessIdRequest(PeerEventContext<ServiceAccessIdRequestEvent> c) {
	ServiceAccessIdRequest request = c.getEvent().getData();
	/* check if the current identity allows this */
	PeerManager peerManager = c.getPeerManager();
	PeerContext peerContext = peerManager.getPeerContext();
	NodeIdentityProfile currentPeerIdentityProfile = peerContext.getCurrentPeerIdentityProfile();

	PeerStateContext peerStateContext = new PeerStateContext(this, peerContext, c.getEvent());

	if (!peerContext.isEventAccepted(peerStateContext)) {
	    /* if the event is not accepted send reject event */
	    ServiceAccessIdRequestRjectedEvent rejectedEvent = new ServiceAccessIdRequestRjectedEvent(
		    new ServiceAccessIdRequestRejectedReason("Request forbidden for your type of authentication"));
	    peerManager.forwardToPeer(rejectedEvent);
	    return;
	}

	EventNodeSecurityManager securityManager = peerContext.getNodeContext().getSecurityManager();

	try {
	    NodeIdentityProfile validIdentityForServiceAccess = securityManager.getValidIdentityForServiceAccess(c,
		    true);
	    NodeIdentity<?> issuedIdentity = validIdentityForServiceAccess.getIdentity();

	    ServiceAccessIdIssuedData serviceAccessIdIssuedData = new ServiceAccessIdIssuedData(issuedIdentity,
		    new ArrayList<>(validIdentityForServiceAccess.getServiceContracts().values()));

	    ServiceAccessIdIssuedEvent serviceAccessIdIssuedEvent = new ServiceAccessIdIssuedEvent(
		    serviceAccessIdIssuedData);

	    peerManager.forwardToPeer(serviceAccessIdIssuedEvent);

	} catch (PeerRequestRejectedException e) {
	    peerManager.forwardToPeer(
		    new ServiceAccessIdRequestRjectedEvent(new ServiceAccessIdRequestRejectedReason(e.getMessage())));

	} catch (PeerRequestHandlingException e) {
	    e.printStackTrace();
	    peerManager.handleError(e);
	}

    }

}
