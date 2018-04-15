package net.segoia.event.eventbus.peers.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocol;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocolConfig;
import net.segoia.event.eventbus.peers.comm.CommunicationProtocolDefinition;
import net.segoia.event.eventbus.peers.comm.EncryptSymmetricOperationDef;
import net.segoia.event.eventbus.peers.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.comm.NodeCommunicationStrategy;
import net.segoia.event.eventbus.peers.comm.PeerCommManager;
import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;
import net.segoia.event.eventbus.peers.events.auth.AuthRejectReason;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRejected;
import net.segoia.event.eventbus.peers.events.auth.id.IdentityType;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.SharedIdentityType;
import net.segoia.event.eventbus.peers.events.auth.id.SharedNodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.peers.events.session.KeyDef;
import net.segoia.event.eventbus.peers.events.session.SessionInfo;
import net.segoia.event.eventbus.peers.events.session.SessionKey;
import net.segoia.event.eventbus.peers.events.session.SessionKeyData;
import net.segoia.event.eventbus.peers.exceptions.PeerAuthRequestRejectedException;
import net.segoia.event.eventbus.peers.exceptions.PeerCommunicationNegotiationFailedException;
import net.segoia.event.eventbus.peers.exceptions.PeerSessionException;
import net.segoia.util.crypto.CryptoUtil;

public class EventNodeSecurityManager {
    private EventNodeSecurityConfig securityConfig;

    private Map<Integer, PrivateIdentityData> privateIdentities = new HashMap<>();

    private Map<Class<? extends NodeIdentity>, PublicIdentityManagerFactory<?>> publicIdentityBuilders;

    private Map<CommManagerKey, CommManagerBuilder> commManagerBuilders;

    private Map<CommManagerKey, CommProtocolContextBuilder<?>> commProtocolContextBuilders;

    private Map<SharedIdentityType, SessionManagerFactory<?>> sessionManagerBuilders;

    public EventNodeSecurityManager() {
	super();
    }

    public EventNodeSecurityManager(EventNodeSecurityConfig securityConfig) {
	super();

	this.securityConfig = securityConfig;// JsonUtils.copyObject(securityConfig);
	loadIdentities();
	initCommProtocolContextBuilders();
	initPublicIdentityBuilders();
	initCommBuilders();
	initSessionManagerBuilders();
    }

    private void initSessionManagerBuilders() {
	sessionManagerBuilders = new HashMap<>();

	sessionManagerBuilders.put(new SharedIdentityType(new KeyDef("AES", 128)),
		new SessionManagerFactory<DefaultSessionManager>() {

		    @Override
		    public DefaultSessionManager build(SharedNodeIdentity identity) {
			return new DefaultSessionManager(identity.getSecretKey());
		    }
		});
    }

    private void initCommProtocolContextBuilders() {
	commProtocolContextBuilders = new HashMap<>();

	commProtocolContextBuilders.put(new CommManagerKey("SPKI", "SPKI", null, PeerCommManager.SESSION_COMM),
		new CommProtocolContextBuilder<SpkiCommProtocolContext>() {

		    @Override
		    public SpkiCommProtocolContext build(PeerCommContext context) {
			PeerContext peerContext = context.getPeerContext();

			SpkiCommProtocolContext commProtocolContext = new SpkiCommProtocolContext(
				context.getTxStrategy().getSessionTxStrategy(),
				context.getRxStrategy().getSessionTxStrategy(),
				(SpkiPrivateIdentityManager) peerContext.getOurIdentityManager(),
				(SpkiPublicIdentityManager) peerContext.getPeerIdentityManager());

			return commProtocolContext;
		    }
		});

	commProtocolContextBuilders.put(new CommManagerKey("SPKI", "SPKI", "AES", PeerCommManager.DIRECT_COMM),
		new CommProtocolContextBuilder<CombinedCommProtocolContext>() {

		    @Override
		    public CombinedCommProtocolContext build(PeerCommContext context) {
			PeerContext peerContext = context.getPeerContext();

			CombinedCommProtocolContext commProtocolContext = new CombinedCommProtocolContext(
				context.getTxStrategy().getDirectTxStrategy(),
				context.getRxStrategy().getDirectTxStrategy(), peerContext.getSessionManager(),
				(SpkiPrivateIdentityManager) peerContext.getOurIdentityManager(),
				(SpkiPublicIdentityManager) peerContext.getPeerIdentityManager());

			return commProtocolContext;
		    }
		});
    }

    private void initPublicIdentityBuilders() {
	if (publicIdentityBuilders == null) {
	    publicIdentityBuilders = new HashMap<>();
	}

	publicIdentityBuilders.put(SpkiNodeIdentity.class, new PublicIdentityManagerFactory<SpkiNodeIdentity>() {

	    @Override
	    public PublicIdentityManager build(SpkiNodeIdentity nodeidentity) {
		return new SpkiPublicIdentityManagerImpl(nodeidentity);
	    }

	});
    }

    private void initCommBuilders() {
	if (commManagerBuilders == null) {
	    commManagerBuilders = new HashMap<>();
	}

	CommManagerConfig config = new CommManagerConfig();

	/* add tx operations */
	config.addTxOperation(SignCommOperationDef.TYPE, new SignCommOperation());
	config.addTxOperation(EncryptWithPublicCommOperationDef.TYPE, new EncryptWithPublicCommOperation());

	/* add rx operations */
	config.addRxOperation(SignCommOperationDef.TYPE, new VerifySignatureCommOperation());
	config.addRxOperation(EncryptWithPublicCommOperationDef.TYPE, new DecryptWithPrivateCommOperation());

	/* add symmetric operations */
	config.addTxOperation(EncryptSymmetricOperationDef.TYPE, new EncryptSymmetricCommOperation());
	config.addRxOperation(EncryptSymmetricOperationDef.TYPE, new DecryptSymmetricCommOperation());

	/* add tx operation context builders */
	config.addTxOpContextBuilder(SignCommOperationDef.TYPE,
		new SpkiCommOperationContextBuilder<SignCommOperationDef>() {

		    @Override
		    public OperationContext buildContext(SignCommOperationDef def, SpkiCommProtocolContext context) {
			return new SignCommOperationContext(def, context.getOurIdentity(), context.getPeerIdentity());
		    }

		});

	config.addTxOpContextBuilder(EncryptWithPublicCommOperationDef.TYPE,
		new SpkiCommOperationContextBuilder<EncryptWithPublicCommOperationDef>() {

		    @Override
		    public OperationContext buildContext(EncryptWithPublicCommOperationDef def,
			    SpkiCommProtocolContext context) {
			return new EncryptWithPublicOperationContext(def, context.getOurIdentity(),
				context.getPeerIdentity());
		    }
		});

	/* add rx operation context builder */

	config.addRxOpContextBuilder(SignCommOperationDef.TYPE,
		new SpkiCommOperationContextBuilder<SignCommOperationDef>() {

		    @Override
		    public OperationContext buildContext(SignCommOperationDef def, SpkiCommProtocolContext context) {
			// byte[] data = opContext.getFullData();
			// String json;
			// try {
			// json = new String(data, "UTF-8");
			// } catch (UnsupportedEncodingException e) {
			// throw new RuntimeException("Failed to convert bytes to UTF-8 string");
			// }
			// SignCommOperationOutput signOperationOutput = JsonUtils.fromJson(json,
			// SignCommOperationOutput.class);

			return new VerifySignatureOperationContext(def, context.getOurIdentity(),
				context.getPeerIdentity());
		    }
		});

	config.addRxOpContextBuilder(EncryptWithPublicCommOperationDef.TYPE,
		new SpkiCommOperationContextBuilder<EncryptWithPublicCommOperationDef>() {

		    @Override
		    public OperationContext buildContext(EncryptWithPublicCommOperationDef def,
			    SpkiCommProtocolContext context) {
			return new DecryptWithPrivateOperationContext(def, context.getOurIdentity(),
				context.getPeerIdentity());
		    }
		});

	/* add symmetric operation context builders */

	config.addTxOpContextBuilder(EncryptSymmetricOperationDef.TYPE,
		new CombinedCommOperationContextBuilder<EncryptSymmetricOperationDef>() {

		    @Override
		    public OperationContext<EncryptSymmetricOperationDef> buildContext(EncryptSymmetricOperationDef def,
			    CombinedCommProtocolContext context) {
			return new EncryptSymmetricCommOperationContext(def, context.getSharedIdentityManager());
		    }
		});

	config.addRxOpContextBuilder(EncryptSymmetricOperationDef.TYPE,
		new CombinedCommOperationContextBuilder<EncryptSymmetricOperationDef>() {

		    @Override
		    public OperationContext<EncryptSymmetricOperationDef> buildContext(EncryptSymmetricOperationDef def,
			    CombinedCommProtocolContext context) {
			return new DecryptSymmetricCommOperationContext(def, context.getSharedIdentityManager());
		    }
		});

	commManagerBuilders.put(new CommManagerKey("SPKI", "SPKI", null, PeerCommManager.SESSION_COMM),
		new SpkiSpkiCommManagerBuilder(config));

	commManagerBuilders.put(new CommManagerKey(null, null, null, PeerCommManager.SESSION_COMM),
		new CommManagerBuilder() {

		    @Override
		    public CommManager build(CommProtocolContext context) {
			return new PlainCommManager();
		    }
		});

	commManagerBuilders.put(new CommManagerKey("SPKI", "SPKI", "AES", PeerCommManager.DIRECT_COMM),
		new CombinedCommManagerBuilder(config));
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

    /**
     * Builds a {@link AbstractCommManager} for a peer
     * 
     * @param context
     * @return
     */
    public CommManager getSessionCommManager(PeerCommContext context) {
	/*
	 * Create a public identity manager for peer and save it on peerContext
	 * 
	 */

	PublicIdentityManager peerIdentity = getPeerIdentity(context);
	PrivateIdentityManager ourIdentity = getOurIdentity(context);

	String ourIdentityType = null;
	if (ourIdentity != null) {
	    ourIdentityType = ourIdentity.getType();
	}

	String peerIdentityType = null;
	if (peerIdentity != null) {
	    peerIdentityType = peerIdentity.getType();
	}

	CommManagerKey commManagerKey = new CommManagerKey(ourIdentityType, peerIdentityType, null,
		PeerCommManager.SESSION_COMM);

	CommProtocolContext commProtocolContext = commProtocolContextBuilders.get(commManagerKey).build(context);

	CommManagerBuilder commManagerBuilder = commManagerBuilders.get(commManagerKey);

	CommManager commManager = commManagerBuilder.build(commProtocolContext);

	return commManager;

    }

    public CommManager getDirectCommManager(PeerCommContext context) {
	PeerContext peerContext = context.getPeerContext();
	CommManagerKey commManagerKey = new CommManagerKey(peerContext.getOurIdentityManager().getType(),
		peerContext.getPeerIdentityManager().getType(), peerContext.getSessionManager().getIdentityType(),
		PeerCommManager.DIRECT_COMM);

	CommManagerBuilder commManagerBuilder = commManagerBuilders.get(commManagerKey);
	SessionManager sessionManager = peerContext.getSessionManager();

	CommProtocolContext commProtocolContext = commProtocolContextBuilders.get(commManagerKey).build(context);

	CommManager commManager = commManagerBuilder.build(commProtocolContext);

	return commManager;
    }

    private PrivateIdentityManager getOurIdentity(PeerCommContext pcc) {

	PeerContext peerContext = pcc.getPeerContext();

	PrivateIdentityManager privateIdentityData = peerContext.getOurIdentityManager();
	if (privateIdentityData != null) {
	    return privateIdentityData;
	}

	privateIdentityData = privateIdentities.get(pcc.getOurIdentityIndex());
	peerContext.setOurIdentityManager(privateIdentityData);
	return privateIdentityData;
    }

    private PublicIdentityManager getPeerIdentity(PeerCommContext pcc) {
	int peerIdentityIndex = pcc.getPeerIdentityIndex();

	PeerContext peerContext = pcc.getPeerContext();

	PublicIdentityManager peerIdentityManager = peerContext.getPeerIdentityManager();
	if (peerIdentityManager != null) {
	    return peerIdentityManager;
	}
	List<? extends NodeIdentity<? extends IdentityType>> peerIdentities = peerContext.getPeerInfo().getNodeAuth()
		.getIdentities();

	if (peerIdentityIndex < 0 || peerIdentityIndex >= peerIdentities.size()) {
	    return null;
	}

	NodeIdentity<? extends IdentityType> nodeIdentity = peerIdentities.get(peerIdentityIndex);

	PublicIdentityManagerFactory ib = publicIdentityBuilders.get(nodeIdentity.getClass());

	if (ib == null) {
	    return null;
	}

	peerIdentityManager = ib.build(nodeIdentity);

	peerContext.setPeerIdentityManager(peerIdentityManager);
	return peerIdentityManager;
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

	List<? extends NodeIdentity<? extends IdentityType>> validPeerIdentities = getValidPeerIdentities(peerContext);

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
	    return CommunicationProtocol.buildPlainProtocol();
	}

	List<? extends NodeIdentity<? extends IdentityType>> localIdentities = securityConfig.getNodeAuth()
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

    private int getNodeIdentityForType(IdentityType type, List<? extends NodeIdentity<?>> identitiesList) {
	int index = 0;
	for (NodeIdentity<?> identity : identitiesList) {
	    if (identity.getType().equals(type)) {
		return index;
	    }
	    index++;
	}
	return -1;
    }

    public SessionKey generateNewSessionKey(PeerContext peerContext) throws PeerSessionException {
	// String channel = peerContext.getCommunicationChannel();
	// PeerChannelSecurityPolicy localChannelPolicy = securityConfig.getSecurityPolicy().getChannelPolicy(channel);
	// ChannelSessionPolicy sessionPolicy = localChannelPolicy.getCommunicationPolicy().getSessionPolicy();

	SharedIdentityType settledSharedIdentityType = (SharedIdentityType) peerContext.getPeerCommContext()
		.getTxStrategy().getSharedIdentityType();

	/*
	 * Get session key definition
	 */
	// KeyDef sessionKeyDef = sessionPolicy.getSessionKeyDef();
	KeyDef sessionKeyDef = settledSharedIdentityType.getKeyDef();

	int maxSupportedKeySize = sessionKeyDef.getKeySize();

	PublicIdentityManager peerIdentityManager = peerContext.getPeerIdentityManager();
	if (peerIdentityManager != null) {
	    maxSupportedKeySize = peerIdentityManager.getMaxSupportedEncryptedDataBlockSize();
	}

	try {
	    SecretKey secretKey = CryptoUtil.generateSecretkey(sessionKeyDef.getAlgorithm(), maxSupportedKeySize);
	    KeyDef newSessionKeyDef = new KeyDef(sessionKeyDef.getAlgorithm(), maxSupportedKeySize);
	    byte[] secretKeyBytes = secretKey.getEncoded();
	    SessionKey sessionKey = new SessionKey(peerContext.getNodeContext().generateSessionId(), secretKeyBytes,
		    newSessionKeyDef);
	    
	    /* generate an initialization vector */
	    SecureRandom sr = new SecureRandom();
	    byte[] iv = new byte[maxSupportedKeySize/8];
	    sr.nextBytes(iv);
	    sessionKey.setIv(iv);

	    /* build a session manager and set it on context */
	    SharedIdentityType sharedIdentityType = new SharedIdentityType(newSessionKeyDef);
	    SessionManager sessionManager = sessionManagerBuilders.get(sharedIdentityType)
		    .build(new SharedNodeIdentity(sharedIdentityType, secretKey));

	    peerContext.setSessionManager(sessionManager);
	    peerContext.setSessionKey(sessionKey);
	    return sessionKey;

	} catch (NoSuchAlgorithmException e) {
	    throw new PeerSessionException("Failed to generate session key with algorithm "
		    + sessionKeyDef.getAlgorithm() + " and size " + maxSupportedKeySize, e);
	}

    }

    public void buildSessionFromSessionInfo(PeerContext peerContext, SessionInfo sessionInfo)
	    throws CommOperationException {
	SessionKeyData sessionKeyData = sessionInfo.getSessionData();
	KeyDef keyDef = sessionKeyData.getKeyDef();
	/* decode base 64 string */
	byte[] sessionTokenBytes = CryptoUtil.base64Decode(sessionKeyData.getSessionToken());
	byte[] sessionSignatureBytes = CryptoUtil.base64Decode(sessionKeyData.getSessionTokenSignature());
	
	/* build a signatureObject */
	SignCommOperationOutput signCommOperationOutput = new SignCommOperationOutput(sessionTokenBytes, sessionSignatureBytes);

	/* now feed this to the session comm manager */

	PeerCommManager peerCommManager = peerContext.getPeerCommManager();

	CommDataContext processedSessionData = peerCommManager
		.processIncomingSessionData(new CommDataContext(signCommOperationOutput));

	SecretKeySpec secretKeySpec = new SecretKeySpec(processedSessionData.getData(), keyDef.getAlgorithm());

	/* build a session manager and set it on context */
	SharedIdentityType sharedIdentityType = new SharedIdentityType(keyDef);
	SessionManager sessionManager = sessionManagerBuilders.get(sharedIdentityType)
		.build(new SharedNodeIdentity(sharedIdentityType, secretKeySpec));

	peerContext.setSessionManager(sessionManager);

	SessionKey sessionKey = new SessionKey(sessionInfo.getSessionId(), sessionTokenBytes, keyDef);
	peerContext.setSessionKey(sessionKey);
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
