package net.segoia.event.eventbus.peers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.segoia.event.eventbus.CustomEventContext;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.peers.events.NewPeerEvent;
import net.segoia.event.eventbus.peers.events.NodeInfo;
import net.segoia.event.eventbus.peers.events.PeerAcceptedEvent;
import net.segoia.event.eventbus.peers.events.PeerInfo;
import net.segoia.event.eventbus.peers.events.PeerLeavingEvent;
import net.segoia.event.eventbus.peers.events.PeerLeftEvent;
import net.segoia.event.eventbus.peers.events.auth.NodeAuth;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthAcceptedEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRejectedEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequestEvent;
import net.segoia.event.eventbus.peers.events.auth.PeerProtocolConfirmedEvent;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequest;
import net.segoia.event.eventbus.peers.events.bind.ConnectToPeerRequestEvent;
import net.segoia.event.eventbus.peers.events.bind.DisconnectFromPeerRequestEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAcceptedEvent;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequest;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequestEvent;
import net.segoia.event.eventbus.peers.events.session.PeerSessionStartedEvent;
import net.segoia.event.eventbus.peers.routing.RoutingTable;
import net.segoia.event.eventbus.peers.security.PrivateIdentityData;
import net.segoia.util.data.SetMap;

public class PeersManager extends GlobalEventNodeAgent {
    private EventNodeContext nodeContext;
    private EventNodePeersRegistry peersRegistry = new EventNodePeersRegistry();
    private RoutingTable routingTable = new RoutingTable();

    private PeerManagerFactory peerManagerFactory;

    public void init(EventNodeContext hostNodeContext) {
	this.nodeContext = hostNodeContext;
	PeersManagerConfig config = hostNodeContext.getConfig().getPeersManagerConfig();
	if (config == null) {
	    config = new PeersManagerConfig();
	}

	peerManagerFactory = new PeerManagerAbstractFactory(config);

	initGlobalContext(new GlobalAgentEventNodeContext(hostNodeContext, this));
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
	    // Event event = c.getEvent();
	    // String peerId = (String) event.getParam(EventParams.peerId);
	    // String lastRelay = event.getLastRelay();
	    // if (lastRelay != null) {
	    // routingTable.addRoute(peerId, lastRelay);
	    // }

	});

	context.addEventHandler("EBUS:PEER:REMOVED", (c) -> {
	    // Event event = c.getEvent();
	    // String removedPeerId = (String) event.getParam(EventParams.peerId);
	    // String lastRelay = event.getLastRelay();
	    // if (lastRelay != null) {
	    // routingTable.removeAllFor(removedPeerId);
	    // }
	});

	context.addEventHandler(PeerLeavingEvent.class, (c) -> {
	    PeerInfo data = c.getEvent().getData().getPeerInfo();
	    String peerId = data.getPeerId();
	    removePeer(peerId);
	    onPeerRemoved(data);
	});

	context.addEventHandler(DisconnectFromPeerRequestEvent.class, (c) -> {
	    String peerId = c.getEvent().getData().getPeerId();
	    PeerManager pm = peersRegistry.getDirectPeerManager(peerId);
	    if (pm != null) {
		pm.terminate();
		removePeer(peerId);
	    } else {
		throw new RuntimeException("No peer manager found for peer id " + peerId);
	    }
	});

	context.addEventHandler(PeerAcceptedEvent.class, (c) -> {
	    handlePeerAccepted(c);
	});

    }

    protected void handlePeerAccepted(CustomEventContext<PeerAcceptedEvent> c) {
	/* mark the peer as direct */
	PeerInfo data = c.getEvent().getData();
	peersRegistry.setPendingPeerAsDirectPeer(data.getPeerId());

	/* Notify everybody that we have a new peer */
	context.postEvent(new NewPeerEvent(data));
    }

    public void handleConnectToPeerRequest(CustomEventContext<ConnectToPeerRequestEvent> c) {
	ConnectToPeerRequest data = c.getEvent().getData();

	EventTransceiver transceiver = data.getTransceiver();

	/* generate a local id for this peer */
	String peerId = nodeContext.generatePeerId();

	/* create a manager for this peer */
	PeerContext peerContext = new PeerContext(peerId, transceiver);

	List<PrivateIdentityData<?>> ourIdentities = data.getOurIdentities();
	if (ourIdentities != null) {

	    peerContext.setOurAvailableIdentities(ourIdentities);
	    
	    NodeInfo defaultNodeInfo = nodeContext.getNodeInfo();
	    
	    /* let's create a custom node info with specified identities */
	    NodeInfo customNodeInfo = new NodeInfo(defaultNodeInfo.getNodeId());
	    customNodeInfo.setSecurityPolicy(defaultNodeInfo.getSecurityPolicy());
	    NodeAuth customNodeAuth = new NodeAuth();
	    List<NodeIdentity<?>> customIdentities = new ArrayList<>();
	    for(PrivateIdentityData<?> pid : ourIdentities) {
		customIdentities.add(pid.getPublicNodeIdentity());
	    }
	    
	    customNodeAuth.setIdentities(customIdentities);
	    customNodeInfo.setNodeAuth(customNodeAuth);
	    
	    peerContext.setOurNodeInfo(customNodeInfo);

	}

	PeerManager peerManager = peerManagerFactory.buildPeerManager(peerContext);
	peerContext.setNodeContext(nodeContext);

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
	PeerManager peerManager = peerManagerFactory.buildPeerManager(new PeerContext(peerId, transceiver));
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

    }

    public void handleProtocolConfirmed(CustomEventContext<PeerProtocolConfirmedEvent> c) {

    }

    public void handleSessionStartedEvent(CustomEventContext<PeerSessionStartedEvent> c) {

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

    protected void updateRoute(Event event) {
	String from = event.from();
	String via = event.getLastRelay();

	if (from != null && !from.equals(via)) {
	    routingTable.addRoute(from, via);
	}
    }

    /**
     * Called when a direct peer has called {@link #terminate()} or simply dropped the connection
     * 
     * @param peerId
     */
    public void onPeerLeaving(PeerLeavingEvent peerLeavingEvent) {

    }

    /**
     * Called after a peer has been unregistered </br>
     * By default sends an EBUS:PEER:REMOVED message </br>
     * Override this if you want specific behavior
     * 
     * @param peerNode
     */
    protected void onPeerRemoved(PeerInfo peerInfo) {
	context.postEvent(new PeerLeftEvent(peerInfo));
    }

    protected void removePeer(String peerId) {
	PeerManager peerManager = peersRegistry.removeDirectPeer(peerId);
	if (peerManager != null) {
	    /* call cleanUp not terminate */
	    peerManager.cleanUp();
	}
	/* remove also pending peers */
	peerManager = peersRegistry.removePendingPeer(peerId);
	if (peerManager != null) {
	    /* call cleanUp not terminate */
	    peerManager.cleanUp();
	}

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

    @Override
    protected void agentInit() {
	// TODO Auto-generated method stub

    }

}
