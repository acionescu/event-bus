package net.segoia.event.eventbus.peers;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.segoia.event.eventbus.CustomEventContext;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.events.NodeInfo;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequest;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequestEvent;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequest;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequestEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAccepted;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAcceptedEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequest;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequestEvent;
import net.segoia.event.eventbus.peers.exceptions.PeerCommunicationNegotiationFailedException;
import net.segoia.event.eventbus.peers.routing.RoutingTable;
import net.segoia.util.data.SetMap;

public class PeersManager {
    private EventNode hostNode;
    private EventNodePeersRegistry peersRegistry = new EventNodePeersRegistry();
    private RoutingTable routingTable = new RoutingTable();

    public PeersManager(EventNode hostNode) {
	super();
	this.hostNode = hostNode;
    }

    public void handleConnectToPeerRequest(CustomEventContext<ConnectToPeerRequestEvent> c) {
	ConnectToPeerRequest data = c.getEvent().getData();

	EventTransceiver transceiver = data.getTransceiver();

	/* generate a local id for this peer */
	String peerId = generatePeerId();

	/* create a relay for this transceiver */
	DefaultEventRelay relay = new DefaultEventRelay(peerId, hostNode, transceiver);

	/* create a manager for this peer */
	PeerManager peerManager = new PeerManager(peerId, relay);

	peerManager.setInServerMode(true);

	peersRegistry.setPendingPeerManager(peerManager);

	peerManager.start();
    }

    public void handlePeerBindRequest(CustomEventContext<PeerBindRequestEvent> c) {

	PeerBindRequest req = c.getEvent().getData();

	EventTransceiver transceiver = req.getTransceiver();

	/* generate a local id for this peer */
	String peerId = generatePeerId();

	/* create a relay for this transceiver */
	DefaultEventRelay relay = new DefaultEventRelay(peerId, hostNode, transceiver);

	/* create a manager for this peer */
	PeerManager peerManager = new PeerManager(peerId, relay);

	peersRegistry.setPendingPeerManager(peerManager);

	peerManager.start();

	PeerBindAccepted resp = new PeerBindAccepted(hostNode.getNodeInfo());

	PeerBindAcceptedEvent respEvent = new PeerBindAcceptedEvent(resp);

	respEvent.to(peerId);

	peerManager.forwardToPeer(respEvent);
    }

    public void handlePeerBindAccepted(CustomEventContext<PeerBindAcceptedEvent> c) {
	PeerBindAcceptedEvent event = c.getEvent();
	PeerBindAccepted resp = event.getData();

	String localPeerId = event.getLastRelay();

	PeerManager peerManager = peersRegistry.getPendingPeerManager(localPeerId);

	if (peerManager == null) {
	    throw new RuntimeException("No peer manager found for localPeerId " + localPeerId);
	    return;
	}

	peerManager.handlePeerBindAccepted(resp);

	PeerAuthRequest peerAuthRequest = new PeerAuthRequest(hostNode.getNodeInfo());

	peerManager.forwardToPeer(new PeerAuthRequestEvent(peerAuthRequest));
    }

    protected void handlePeerAuthRequest(CustomEventContext<PeerAuthRequestEvent> c) {
	PeerAuthRequestEvent event = c.getEvent();
	PeerAuthRequest data = event.getData();

	String localPeerId = event.getLastRelay();

	PeerManager peerManager = peersRegistry.getPendingPeerManager(localPeerId);

	if (peerManager == null) {
	    throw new RuntimeException("No peer manager found for localPeerId " + localPeerId);
	    return;
	}

	peerManager.handlePeerAuthRequest(data);

	
	try {
	    CommunicationProtocol commProtocol = hostNode.getSecurityManager().establishPeerCommunicationProtocol(peerManager.getPeerContext());
	    CommunicationProtocol protocolProposedByPeer = data.getCommunicationProtocol();
	    if(protocolProposedByPeer != null) {
		if(commProtocol.equals(protocolProposedByPeer)) {
		    /* The protocol chose by us matches with the one proposed by peer. All is good */
		    peerManager.getPeerContext().setCommProtocol(commProtocol);
		    
		    //TODO: send auth accepted event
		    
		}
		else {
		    /* Ups, the proposed protocol doesn't match with the one we found */
		    //TODO: Propose the one we found
		}
	    }
	    else {
		//TODO: propose the protocol we found
	    }
	    
	    
	} catch (PeerCommunicationNegotiationFailedException ex) {

	}

    }

    protected void forwardToAll(Event event) {
	EventContext ec = new EventContext(event, null);
	for (PeerManager peerContext : peersRegistry.getDirectPeers().values()) {
	    EventRelay r = peerContext.getRelay();
	    r.onLocalEvent(ec);
	}
    }

    protected void forwardTo(Event event, String to) {
	event.to(to);
	if (getId().equals(to)) {
	    postInternally(event);
	    return;
	}

	EventContext ec = new EventContext(event, null);
	/* first check if the destination is one of the peers */
	EventRelay peerRelay = peersRegistry.getDirectPeerRelay(to);
	/* if it is, forward the event */
	if (peerRelay != null) {
	    peerRelay.onLocalEvent(ec);
	}
	/* otherwise, set destination and forward it to the peers */
	else if (!event.wasRelayedBy(getId())) {
	    forwardToAll(event);
	}
    }

    protected EventRelay getRelayForPeer(String peerId) {
	/* check direct peers */
	EventRelay peerRelay = getDirectPeerRelay(peerId);
	if (peerRelay == null) {
	    /* check remote peers */
	    peerRelay = getRemotePeerRelay(peerId);
	}

	return peerRelay;
    }

    protected void forwardTo(Event event, Set<String> peerIds) {
	/* check if if we are targeted by the event as well */
	if (peerIds.contains(getId())) {
	    postInternally(event);
	    return;
	}

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
	    if (getDirectPeerRelay(cto) != null || getId().equals(cto)) {
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
		System.err.println(getId() + ": Couldn't find a via for " + cto + " , forwarding to all");
		event.setForwardTo(peerIds);
		forwardToAll(event);
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
		EventRelay viaRelay = getDirectPeerRelay(via);

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
		.filter((e) -> e.getValue().getRelay().isForwardingAllowed(ec)).map((e) -> e.getKey());
	Stream<String> rpStream = peersRegistry.getRemotePeers().entrySet().stream()
		.filter((e) -> e.getValue().getRelay().isForwardingAllowed(ec)).map((e) -> e.getKey());
	Set<String> targetedPeers = Stream.concat(dpStream, rpStream).collect(Collectors.toSet());

	return targetedPeers;
    }

    protected EventRelay getDirectPeerRelay(String id) {
	return peersRegistry.getDirectPeerRelay(id);
    }

    protected EventRelay getRemotePeerRelay(String id) {
	return peersRegistry.getRemotePeerRelay(id);
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
	else if (config.isAutoRelayEnabled() && !event.wasRelayedBy(peerId)) {
	    return true;
	}

	return false;
    }

    protected static String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    protected static String generateSessionId() {
	return UUID.randomUUID().toString();
    }

    protected static String generateSecurityToken() {
	return UUID.randomUUID().toString();
    }
}
