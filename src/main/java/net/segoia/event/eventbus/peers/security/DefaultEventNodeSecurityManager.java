package net.segoia.event.eventbus.peers.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.SecretKey;

import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.core.PublicIdentityManager;
import net.segoia.event.eventbus.peers.core.PublicIdentityManagerFactory;
import net.segoia.event.eventbus.peers.exceptions.PeerSessionException;
import net.segoia.event.eventbus.peers.manager.states.server.PeerAcceptedState;
import net.segoia.event.eventbus.peers.security.rules.PeerEventBlackList;
import net.segoia.event.eventbus.peers.security.rules.PeerEventRule;
import net.segoia.event.eventbus.peers.security.rules.PeerRuleEngine;
import net.segoia.event.eventbus.peers.vo.auth.id.SharedIdentityType;
import net.segoia.event.eventbus.peers.vo.auth.id.SharedNodeIdentity;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.peers.vo.session.KeyDef;
import net.segoia.event.eventbus.peers.vo.session.SessionKey;
import net.segoia.event.eventbus.util.JsonUtils;
import net.segoia.util.crypto.CryptoUtil;

public class DefaultEventNodeSecurityManager extends EventNodeSecurityManager {

    public DefaultEventNodeSecurityManager() {
	super();
	// TODO Auto-generated constructor stub
    }

    public DefaultEventNodeSecurityManager(EventNodeSecurityConfig securityConfig) {
	super(securityConfig);
	// TODO Auto-generated constructor stub
    }
    
    

    @Override
    protected void init() {
	setCryptoHelper(new DefaultCryptoHelper());
	super.init();
    }

    @Override
    protected void initSessionManagerBuilders() {
	sessionManagerBuilders = new HashMap<>();

	sessionManagerBuilders.put(new SharedIdentityType(new KeyDef("AES", 128)),
		new SessionManagerFactory<DefaultSessionManager>() {

		    @Override
		    public DefaultSessionManager build(SharedNodeIdentity identity) {
			return new DefaultSessionManager(identity);
		    }
		});
    }

    @Override
    protected void initPublicIdentityBuilders() {
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

    @Override
    protected void initSerializers() {
	defaultDataSerializer = new NestedOperationDataSerializer((o) -> {
	    return o.getData();
	});

	defaultDataSerializer.addSerializer(SignCommOperationOutput.class, (o) -> {

	    try {
		return JsonUtils.toJson(o).getBytes("UTF-8");
	    } catch (UnsupportedEncodingException e) {

		e.printStackTrace();
	    }
	    return null;
	});
    }

    @Override
    protected void initIdentityRoles() {
	PeerRuleEngine peerRuleEngine = getPeerRuleEngine();
	if(peerRuleEngine == null) {
	    peerRuleEngine = new PeerRuleEngine();
	    peerRuleEngine.setRuleMatcherFactory(new DefaultRuleMatcherFactory());
	    setPeerRuleEngine(peerRuleEngine);
	}
	
	List<PeerEventRule> serviceAccessRules = new ArrayList<>();
	serviceAccessRules.add(peerRuleEngine.buildRegexRule(PeerAcceptedState.ID + "/SERVICE:ACCESS_ID:REQUEST"));

	PeerEventBlackList deniedServiceAccessIdEvents = new PeerEventBlackList(serviceAccessRules);

	IdentityRole serviceAccessIdRole = new IdentityRole(IdentityRole.SERVICE_ACCESS, deniedServiceAccessIdEvents);

	identityRoles.put(serviceAccessIdRole.getType(), serviceAccessIdRole);

	identityRoles.put(IdentityRole.PEER_AUTH, new IdentityRole(IdentityRole.PEER_AUTH, null));
    }

    @Override
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
	    byte[] iv = new byte[maxSupportedKeySize / 8];
	    sr.nextBytes(iv);
	    sessionKey.setIv(iv);

	    /* build a session manager and set it on context */
	    SharedIdentityType sharedIdentityType = new SharedIdentityType(newSessionKeyDef);
	    SessionManager sessionManager = sessionManagerBuilders.get(sharedIdentityType)
		    .build(new SharedNodeIdentity(sharedIdentityType, secretKeyBytes, iv));

	    peerContext.setSessionManager(sessionManager);
	    peerContext.setSessionKey(sessionKey);
	    return sessionKey;

	} catch (NoSuchAlgorithmException e) {
	    throw new PeerSessionException("Failed to generate session key with algorithm "
		    + sessionKeyDef.getAlgorithm() + " and size " + maxSupportedKeySize, e);
	}

    }

    @Override
    protected void initDeserializers() {
	deserializers = new HashMap<>();
	deserializers.put(SignCommOperationOutput.class, (data) -> {

	    String json;
	    try {
		json = new String(data, "UTF-8");
	    } catch (UnsupportedEncodingException e) {
		throw new RuntimeException("Failed to convert bytes to UTF-8 string");
	    }
	    SignCommOperationOutput signOperationOutput = JsonUtils.fromJson(json,
		    SignCommOperationOutput.class);
	    return signOperationOutput;
	});
    }

}
