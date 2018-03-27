package net.segoia.event.eventbus.peers;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.segoia.event.eventbus.CustomEventContext;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.constants.EventParams;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.events.PeerLeavingEvent;
import net.segoia.event.eventbus.peers.events.SessionInfo;
import net.segoia.event.eventbus.peers.events.auth.AuthRejectReason;
import net.segoia.event.eventbus.peers.events.auth.MessageAuthRejectedReason;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthAccepted;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthAcceptedEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRejected;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRejectedEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequest;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequestEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerProtocolConfirmedEvent;
import net.segoia.event.eventbus.peers.events.auth.ProtocolConfirmation;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequest;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequestEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAccepted;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAcceptedEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequest;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequestEvent;
import net.segoia.event.eventbus.peers.events.register.PeerRegisterRequestEvent;
import net.segoia.event.eventbus.peers.events.register.PeerRegisteredEvent;
import net.segoia.event.eventbus.peers.events.register.PeerRequestUnregisterEvent;
import net.segoia.event.eventbus.peers.events.register.PeerUnregisteredEvent;
import net.segoia.event.eventbus.peers.events.register.PeerRegisterRequestEvent.Data;
import net.segoia.event.eventbus.peers.events.session.PeerSessionStartedEvent;
import net.segoia.event.eventbus.peers.events.session.SessionStartedData;
import net.segoia.event.eventbus.peers.exceptions.PeerAuthRequestRejectedException;
import net.segoia.event.eventbus.peers.exceptions.PeerCommunicationNegotiationFailedException;
import net.segoia.event.eventbus.peers.routing.RoutingTable;
import net.segoia.util.data.SetMap;

public class PeersManager extends GlobalEventNodeAgent {
    private EventNodeContext nodeContext;
    private EventNodePeersRegistry peersRegistry = new EventNodePeersRegistry();
    private RoutingTable routingTable = new RoutingTable();

    public void init(EventNodeContext hostNodeContext) {
	this.nodeContext = hostNodeContext;
	initGlobalContext(new GlobalAgentEventNodeContext(hostNodeContext, this));
    }

    @Override
    protected void init() {
	// TODO Auto-generated method stub

    }

    @Override
    public void terminate() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void config() {
	// TODO Auto-generated method stub

    }

    @Override
    protected void registerHandlers() {
	context.addEventHandler(ConnectToPeerRequestEvent.class, (c) -> {
	    handleConnectToPeerRequest(c);
	});

	context.addEventHandler(PeerBindRequestEvent.class, (c) -> {
	    handlePeerBindRequest(c);

	});

	context.addEventHandler(PeerBindAcceptedEvent.class, (c) -> {
	    handlePeerBindAccepted(c);

	});

	context.addEventHandler(PeerAuthRequestEvent.class, (c) -> {
	    handlePeerAuthRequest(c);
	});

	context.addEventHandler(PeerAuthRejectedEvent.class, (c) -> {
	    handlePeerAuthRejected(c);
	});

	context.addEventHandler("EBUS:PEER:NEW", (c) -> {
	    Event event = c.getEvent();
	    String peerId = (String) event.getParam(EventParams.peerId);
	    String lastRelay = event.getLastRelay();
	    if (lastRelay != null) {
		routingTable.addRoute(peerId, lastRelay);
	    }

	});

	context.addEventHandler("EBUS:PEER:REMOVED", (c) -> {
	    Event event = c.getEvent();
	    String removedPeerId = (String) event.getParam(EventParams.peerId);
	    String lastRelay = event.getLastRelay();
	    if (lastRelay != null) {
		routingTable.removeAllFor(removedPeerId);
	    }
	});
	
	context.addEventHandler(PeerLeavingEvent.class, (c) -> {
	    String peerId = c.getEvent().getData().getNodeId();
	    removePeer(peerId);
	    onPeerRemoved(peerId);
	});
	
    }
    
    public void handleConnectToPeerRequest(CustomEventContext<ConnectToPeerRequestEvent> c) {
	ConnectToPeerRequest data = c.getEvent().getData();

	EventTransceiver transceiver = data.getTransceiver();

	/* generate a local id for this peer */
	String peerId = nodeContext.generatePeerId();

	/* create a manager for this peer */
	PeerManager peerManager = new PeerManager(peerId, transceiver);
	peerManager.getPeerContext().setNodeContext(nodeContext);

	peerManager.setInServerMode(true);

	peersRegistry.setPendingPeerManager(peerManager);

	peerManager.start();
    }

    public void handlePeerBindRequest(CustomEventContext<PeerBindRequestEvent> c) {

	PeerBindRequest req = c.getEvent().getData();

	EventTransceiver transceiver = req.getTransceiver();

	/* generate a local id for this peer */
	String peerId = nodeContext.generatePeerId();

	/* create a manager for this peer */
	PeerManager peerManager = new PeerManager(peerId, transceiver);
	peerManager.getPeerContext().setNodeContext(nodeContext);

	peersRegistry.setPendingPeerManager(peerManager);

	peerManager.start();

	
    }

    public void handlePeerBindAccepted(CustomEventContext<PeerBindAcceptedEvent> c) {
	
    }

    public void handlePeerAuthRequest(CustomEventContext<PeerAuthRequestEvent> c) {
	
    }

    public void handlePeerAuthRejected(CustomEventContext<PeerAuthRejectedEvent> c) {
	// TODO: implement this
    }

    public void handlePeerAuthAccepted(CustomEventContext<PeerAuthAcceptedEvent> c) {
	PeerAuthAcceptedEvent event = c.getEvent();
	PeerManager peerManager = getPeerManagerForEvent(event);

	PeerAuthAccepted data = event.getData();

	CommunicationProtocol protocolFromPeer = data.getCommunicationProtocol();

	PeerContext peerContext = peerManager.getPeerContext();
	CommunicationProtocol ourProtocol = peerContext.getCommProtocol();

	if (ourProtocol == null) {
	    /* if we hanve't proposed a protocol, see if we can find a matching supported protocol */
	    try {
		ourProtocol = nodeContext.getSecurityManager().establishPeerCommunicationProtocol(peerContext);
	    } catch (PeerCommunicationNegotiationFailedException ex) {

	    } catch (PeerAuthRequestRejectedException arex) {

	    }

	    /* set the protocol on peer context */
	    peerContext.setCommProtocol(ourProtocol);
	}

	/* check that the two protocols match */

	if (ourProtocol.equals(protocolFromPeer)) {
	    /* yey, we have a matching protocol, send confirmation */
	    ProtocolConfirmation protocolConfirmation = new ProtocolConfirmation(ourProtocol);
	    peerManager.forwardToPeer(new PeerProtocolConfirmedEvent(protocolConfirmation));
	}
    }

    public void handleProtocolConfirmed(CustomEventContext<PeerProtocolConfirmedEvent> c) {
	PeerProtocolConfirmedEvent event = c.getEvent();
	PeerManager peerManager = getPeerManagerForEvent(event);

	ProtocolConfirmation data = event.getData();

	/* check again if the protocols match */
	CommunicationProtocol ourProtocol = peerManager.getPeerContext().getCommProtocol();
	CommunicationProtocol peerProtocol = data.getProtocol();

	if (!ourProtocol.equals(peerProtocol)) {
	    /* ups, they don't match */

	    // TODO: handle this
	}

	/* if they match, initiate the session */

	SessionStartedData sessionStartedData = new SessionStartedData(generateNewSession());

	peerManager.forwardToPeer(new PeerSessionStartedEvent(sessionStartedData));

    }

    public void handleSessionStartedEvent(CustomEventContext<PeerSessionStartedEvent> c) {
	PeerSessionStartedEvent event = c.getEvent();
	PeerManager peerManager = getPeerManagerForEvent(event);

	SessionStartedData data = event.getData();

	peerManager.getPeerContext().setSessionInfo(data.getSessionInfo());

	peerManager.onReady();
    }

    protected PeerManager getPeerManagerById(String peerId) {
	return peersRegistry.getDirectPeerManager(peerId);
    }

    protected PeerManager getPeerManagerForEvent(Event event) {
	String localPeerId = event.getLastRelay();
	return peersRegistry.getPendingPeerManager(localPeerId);
    }

    

    protected String getLocalNodeId() {
	return nodeContext.getLocalNodeId();
    }

    protected void forwardToDirectPeers(Event event) {
	EventContext ec = new EventContext(event, null);
	for (PeerManager peerManager : peersRegistry.getDirectPeers().values()) {
	    // EventRelay r = peerManager.getRelay();
	    // r.onLocalEvent(ec);
	    peerManager.forwardToPeer(event);
	}
    }

    protected void forwardTo(Event event, String to) {
	event.to(to);
	// if (hostNode.getId().equals(to)) {
	// postInternally(event);
	// return;
	// }

	EventContext ec = new EventContext(event, null);
	/* first check if the destination is one of the peers */
	PeerManager peerManager = peersRegistry.getDirectPeerManager(to);
	/* if it is, forward the event */
	if (peerManager != null) {
	    peerManager.forwardToPeer(event);
	}
	/* otherwise, set destination and forward it to the peers */
	else if (!event.wasRelayedBy(getLocalNodeId())) {
	    forwardToDirectPeers(event);
	}
    }

    // protected EventRelay getRelayForPeer(String peerId) {
    // /* check direct peers */
    // EventRelay peerRelay = getDirectPeer(peerId);
    // if (peerRelay == null) {
    // /* check remote peers */
    // peerRelay = getRemotePeerManager(peerId);
    // }
    //
    // return peerRelay;
    // }

    protected void forwardTo(Event event, Set<String> peerIds) {
	/* check if if we are targeted by the event as well */
	// if (peerIds.contains(getLocalNodeId())) {
	// postInternally(event);
	// return;
	// }

	/* only forward further, if there are other targeted nodes except us */
	if (peerIds.size() <= 0) {
	    return;
	}
	/* Keep the peers indexed by the next hop in the path to them */
	SetMap<String, String> peersByVia = new SetMap<>();

	EventContext ec = new EventContext(event, null);
	for (String cto : peerIds) {
	    String cvia = null;
	    /* if this is a direct peer or us, use targeted peer id as via */
	    if (getDirectPeer(cto) != null || getLocalNodeId().equals(cto)) {
		cvia = cto;
	    } else {
		/* if it's a remote peer, we should have a via in the routing table */
		cvia = routingTable.getBestViaTo(cto);
	    }

	    if (cvia == null) {
		/*
		 * if we can't find a via for a remote node, then forward to all, however this should not happen under
		 * normal conditions
		 */
		System.err.println(getLocalNodeId() + ": Couldn't find a via for " + cto + " , forwarding to all");
		event.setForwardTo(peerIds);
		forwardToDirectPeers(event);
		return;
	    } else {
		peersByVia.add(cvia, cto);
	    }
	}

	/* now we have to send an event to each via */
	for (Map.Entry<String, Set<String>> re : peersByVia.entrySet()) {
	    /*
	     * we have to create a new event with the header field forwardTo updated with the peers that we can reach
	     * through each via
	     */

	    /* if we want to forward messages via this relay, we have to add the nodes in the forwardTo field */
	    String via = re.getKey();

	    Set<String> ftp = peersByVia.get(via);
	    if (ftp != null) {
		/* if our rules forbid us to forward to this node, then don't bother */
		if (!isEventForwardingAllowed(ec, via)) {
		    continue;
		}
		EventRelay viaRelay = getDirectPeer(via).getPeerContext().getRelay();

		/* clone event, rewrite forwardTo and send it directly */
		Event ce = event.clone();
		ce.setForwardTo(ftp);

		/* since this is a forward, we will send directly, regardless of the rules of the peer */
		viaRelay.sendEvent(ce);

	    }
	}
    }

    protected void forwardToAllKnown(Event event) {
	EventContext ec = new EventContext(event, null);
	Set<String> targetedPeers = getKnownPeers(ec);

	event.setForwardTo(targetedPeers);
	forwardTo(event, targetedPeers);

    }

    protected Set<String> getKnownPeers(EventContext ec) {

	Stream<String> dpStream = peersRegistry.getDirectPeers().entrySet().stream()
		.filter((e) -> e.getValue().getPeerContext().getRelay().isForwardingAllowed(ec)).map((e) -> e.getKey());
	Stream<String> rpStream = peersRegistry.getRemotePeers().entrySet().stream()
		.filter((e) -> e.getValue().getPeerContext().getRelay().isForwardingAllowed(ec)).map((e) -> e.getKey());
	Set<String> targetedPeers = Stream.concat(dpStream, rpStream).collect(Collectors.toSet());

	return targetedPeers;
    }

    protected PeerManager getDirectPeer(String id) {
	return peersRegistry.getDirectPeerManager(id);
    }

    protected PeerManager getRemotePeerManager(String id) {
	return peersRegistry.getRemotePeerManager(id);
    }

    public boolean isEventForwardingAllowed(EventContext ec, String peerId) {
	Event event = ec.event();

	/* if a destination is specified, only forward to that peerId */
	String destination = event.to();
	if (destination != null && !event.wasRelayedBy(peerId)) {
	    return true;
	}

	/* if this wasn't forwarded allow it */
	String sourceBusId = event.from();
	if (sourceBusId == null) {
	    return true;
	}
	/* don't forward an event to a peer that already relayed that event */
	else if (nodeContext.getConfig().isAutoRelayEnabled() && !event.wasRelayedBy(peerId)) {
	    return true;
	}

	return false;
    }

    protected SessionInfo generateNewSession() {
	return new SessionInfo(nodeContext.generateSessionId(), nodeContext.generateSecurityToken());
    }

    protected void updateRoute(Event event) {
	String from = event.from();
	String via = event.getLastRelay();

	if (from != null && !from.equals(via)) {
	    routingTable.addRoute(from, via);
	}
    }

    /**
     * Called when a direct peer has called {@link #terminate()}
     * 
     * @param peerId
     */
    public void onPeerLeaving(String peerId) {
	/* process this through the internal bus */
	context.postEvent(new PeerLeavingEvent(peerId));
    }

    /**
     * Called after a peer has been unregistered </br>
     * By default sends an EBUS:PEER:REMOVED message </br>
     * Override this if you want specific behavior
     * 
     * @param peerNode
     */
    protected void onPeerRemoved(String peerId) {
	Event nne = Events.builder().ebus().peer().peerRemoved().build();
	nne.addParam(EventParams.peerId, peerId);
	forwardToDirectPeers(nne);
    }

    protected void removePeer(String peerId) {
	PeerManager peerManager = peersRegistry.getDirectPeerManager(peerId);
	if (peerManager != null) {
	    /* call cleanUp not terminate */
	    peerManager.cleanUp();
	}
	peersRegistry.removeDirectPeer(peerId);
	routingTable.removeAllFor(peerId);
    }

    /**
     * Called after a new peer has been registered </br>
     * By default sends an EBUS:PEER:NEW event </br>
     * Override this if you want specific behavior
     * 
     * @param peerNode
     */
    protected void onNewPeer(String peerId) {
	if (peersRegistry.getDirectPeerManager(peerId).isRemoteAgent()) {
	    peersRegistry.addAgent(peerId);
	    /* don't broadcast agents as peers, this is private business */
	    return;
	}
	Event nne = Events.builder().ebus().peer().newPeer().build();
	nne.addParam("peerId", peerId);
	forwardToDirectPeers(nne);
    }

  

}
