package net.segoia.event.eventbus.peers.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocolConfig;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocolDefinition;
import net.segoia.event.eventbus.peers.comm.NodeCommunicationStrategy;
import net.segoia.event.eventbus.peers.comm.PlainCommunicationProtocol;
import net.segoia.event.eventbus.peers.events.auth.AuthRejectReason;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRejected;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentityType;
import net.segoia.event.eventbus.peers.exceptions.PeerAuthRequestRejectedException;
import net.segoia.event.eventbus.peers.exceptions.PeerCommunicationNegotiationFailedException;

public class EventNodeSecurityManager {
    private EventNodeSecurityConfig securityConfig;

    private Map<Integer, PrivateIdentityData> privateIdentities = new HashMap<>();

    public EventNodeSecurityManager() {
	super();
    }

    public EventNodeSecurityManager(EventNodeSecurityConfig securityConfig) {
	super();

	this.securityConfig = securityConfig;// JsonUtils.copyObject(securityConfig);
	loadIdentities();
    }

    private void loadIdentities() {
	List<PrivateIdentityDataLoader<?>> identityLoaders = securityConfig.getIdentityLoaders();
	if (identityLoaders == null) {
	    return;
	}

	List nodeIdentities = securityConfig.getNodeAuth().getIdentities();

	if (nodeIdentities == null) {
	    nodeIdentities = new ArrayList<>();
	    securityConfig.getNodeAuth().setIdentities(nodeIdentities);
	}

	for (PrivateIdentityDataLoader<?> l : identityLoaders) {
	    l.load();
	    PrivateIdentityData<?> data = (PrivateIdentityData) l.getData();

	    /* save private identity under the index of the position in the node identities */
	    int index = nodeIdentities.size();
	    privateIdentities.put(index, data);
	    data.setIndex(index);

	    NodeIdentity publicNodeIdentity = data.getPublicNodeIdentity();

	    nodeIdentities.add(publicNodeIdentity);
	}
    }
    
    
    public Event processOutgoingEvent(CommProtocolContext context) {
	
    }

    /**
     * Override this to filter through the peer provided identities
     * 
     * @param peerContext
     * @return
     */
    public List<? extends NodeIdentity<?>> getValidPeerIdentities(PeerContext peerContext) {
	return peerContext.getPeerInfo().getNodeAuth().getIdentities();
    }

    public CommunicationProtocol establishPeerCommunicationProtocol(PeerContext peerContext)
	    throws PeerCommunicationNegotiationFailedException, PeerAuthRequestRejectedException {

	List<? extends NodeIdentity<? extends NodeIdentityType>> validPeerIdentities = getValidPeerIdentities(
		peerContext);

	if (validPeerIdentities.size() == 0) {
	    /* if no valid peer identity found, throw an exception */
	    throw new PeerAuthRequestRejectedException(
		    new PeerAuthRejected(new AuthRejectReason("No valid peer identity provided")));

	}

	/* Get the most preferred matching protocol */

	String channel = peerContext.getCommunicationChannel();

	PeerChannelSecurityPolicy localChannelPolicy = securityConfig.getSecurityPolicy().getChannelPolicy(channel);
	PeerChannelSecurityPolicy peerChannelPolicy = peerContext.getPeerInfo().getSecurityPolicy()
		.getChannelPolicy(channel);

	ChannelCommunicationPolicy localCommPolicy = localChannelPolicy.getCommunicationPolicy();
	ChannelCommunicationPolicy peerCommPolicy = peerChannelPolicy.getCommunicationPolicy();

	/* if now communication policy is set, use plain protocol */
	if (localCommPolicy == null && peerCommPolicy == null) {
	    return new PlainCommunicationProtocol();
	}

	List<? extends NodeIdentity<? extends NodeIdentityType>> localIdentities = securityConfig.getNodeAuth()
		.getIdentities();

	/* see if we can match a local tx strategy with a peer rx strategy */

	List<StrategyIdentitiesPair> localAsTxStrategyIdentitiesPairs = getMatchingCommStrategy(
		localCommPolicy.getSupportedTxStrategies(), peerCommPolicy.getSupportedRxStrategies(), localIdentities,
		validPeerIdentities);

	if (localAsTxStrategyIdentitiesPairs.size() == 0) {
	    throw new PeerCommunicationNegotiationFailedException("Can't find a supported local tx strategy");
	}

	/* see if we can match a local rx strategy with a peer tx strategy */
	List<StrategyIdentitiesPair> peerAsTxStrategyIdentitiesPairs = getMatchingCommStrategy(
		peerCommPolicy.getSupportedTxStrategies(), localCommPolicy.getSupportedRxStrategies(),
		validPeerIdentities, localIdentities);

	if (peerAsTxStrategyIdentitiesPairs.size() == 0) {
	    throw new PeerCommunicationNegotiationFailedException("Can't find a supported local rx strategy");
	}

	/* we now have to find the pairs that use the same identities */

	for (StrategyIdentitiesPair localAsTxPair : localAsTxStrategyIdentitiesPairs) {
	    for (StrategyIdentitiesPair peerAsTxPair : peerAsTxStrategyIdentitiesPairs) {
		if (localAsTxPair.txRxMatches(peerAsTxPair)) {
		    /* we have found supported tx and rx strategies using one identity for each node */
		    if (peerContext.isInServerMode()) {
			return buildCommunicationProtocol(peerAsTxPair, localAsTxPair);
		    } else {
			return buildCommunicationProtocol(localAsTxPair, peerAsTxPair);
		    }
		}
	    }
	}

	throw new PeerCommunicationNegotiationFailedException(
		"Failed to find a protocol for the provided identities and strategies");
    }

    private CommunicationProtocol buildCommunicationProtocol(StrategyIdentitiesPair serverPair,
	    StrategyIdentitiesPair clientPair) {
	CommunicationProtocolDefinition communicationProtocolDefinition = new CommunicationProtocolDefinition(
		serverPair.strategy, serverPair.strategy);
	CommunicationProtocolConfig communicationProtocolConfig = new CommunicationProtocolConfig(
		serverPair.txNodeIdentity, serverPair.rxNodeIdentity);
	return new CommunicationProtocol(communicationProtocolDefinition, communicationProtocolConfig);
    }

    /**
     * Returns all strategy identities pairs found
     * 
     * @param txStrategies
     * @param rxStrategies
     * @param txNodeIdentities
     * @param rxNodeIdentities
     * @return
     */
    private List<StrategyIdentitiesPair> getMatchingCommStrategy(List<NodeCommunicationStrategy> txStrategies,
	    List<NodeCommunicationStrategy> rxStrategies, List<? extends NodeIdentity<?>> txNodeIdentities,
	    List<? extends NodeIdentity<?>> rxNodeIdentities) {

	List<StrategyIdentitiesPair> strategyIdentitiesPairs = new ArrayList<>();

	for (NodeCommunicationStrategy txs : txStrategies) {
	    for (NodeCommunicationStrategy rxs : rxStrategies) {
		if (txs != null && txs.equals(rxs)) {
		    /*
		     * cool, we have found a matching strategy, now let's see if we find matching identities with this
		     * as well
		     */
		    int txNodeIdentity = getNodeIdentityForType(txs.getTxNodeIdentityType(), txNodeIdentities);
		    if (txNodeIdentity < 0) {
			/* not found */
			continue;
		    }

		    int rxNodeIdentity = getNodeIdentityForType(txs.getRxNodeIdentityType(), rxNodeIdentities);

		    if (rxNodeIdentity < 0) {
			/* not found */
			continue;
		    }

		    /* we have found a strategy and identities pair, add it to the list */
		    strategyIdentitiesPairs.add(new StrategyIdentitiesPair(txs, txNodeIdentity, rxNodeIdentity));
		}
	    }
	}
	return strategyIdentitiesPairs;
    }

    private int getNodeIdentityForType(NodeIdentityType type, List<? extends NodeIdentity<?>> identitiesList) {
	int index = 0;
	for (NodeIdentity<?> identity : identitiesList) {
	    if (identity.getType().equals(type)) {
		return index;
	    }
	    index++;
	}
	return -1;
    }

    private class StrategyIdentitiesPair {
	NodeCommunicationStrategy strategy;
	int txNodeIdentity;
	int rxNodeIdentity;

	public StrategyIdentitiesPair(NodeCommunicationStrategy strategy, int txNodeIdentity, int rxNodeIdentity) {
	    super();
	    this.strategy = strategy;
	    this.txNodeIdentity = txNodeIdentity;
	    this.rxNodeIdentity = rxNodeIdentity;
	}

	/**
	 * Checks if another pair matches as a communication partner
	 * 
	 * @param peerPair
	 * @return
	 */
	public boolean txRxMatches(StrategyIdentitiesPair peerPair) {
	    return (txNodeIdentity == peerPair.rxNodeIdentity) && (rxNodeIdentity == peerPair.txNodeIdentity);
	}
    }

}
