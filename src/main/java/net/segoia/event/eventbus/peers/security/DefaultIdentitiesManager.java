package net.segoia.event.eventbus.peers.security;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import net.segoia.event.eventbus.peers.core.IdentitiesManager;
import net.segoia.event.eventbus.peers.core.IdentitiesRepository;
import net.segoia.event.eventbus.peers.core.IdentityBuilder;
import net.segoia.event.eventbus.peers.core.IdentityException;
import net.segoia.event.eventbus.peers.vo.auth.id.IdentityType;
import net.segoia.event.eventbus.peers.vo.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiFullIdentityType;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiFullNodeIdentity;
import net.segoia.event.eventbus.peers.vo.security.IssueIdentityRequest;
import net.segoia.event.eventbus.peers.vo.session.KeyDef;
import net.segoia.event.eventbus.vo.services.NodeIdentityProfile;
import net.segoia.util.crypto.CryptoUtil;

public class DefaultIdentitiesManager implements IdentitiesManager {
    private Map<Class<?>, IdentityBuilder<?>> identityBuilders;
    private IdentitiesRepository identitiesRepository = new InMemoryIdentitiesRepository();

    public DefaultIdentitiesManager() {
	identityBuilders = new HashMap<>();
	identityBuilders.put(SpkiFullIdentityType.class, new IdentityBuilder<SpkiFullNodeIdentity>() {

	    @Override
	    public SpkiFullNodeIdentity buildIdentity(IssueIdentityRequest request) {
		SpkiFullIdentityType identityType = (SpkiFullIdentityType) request.getIdentityType();
		KeyDef keyDef = identityType.getKeyDef();
		try {
		    KeyPair keyPair = CryptoUtil.generateKeyPair(keyDef.getAlgorithm(), keyDef.getKeySize());
		    String encodedPubKey = CryptoUtil.encodePublicKeyToBase64String(keyPair.getPublic());
		    String encodedPrivateKey = CryptoUtil.encodePrivateKeyToBase64String(keyPair.getPrivate());

		    SpkiFullNodeIdentity result = new SpkiFullNodeIdentity(keyDef);
		    result.setPrivateKey(encodedPrivateKey);
		    result.setPubKey(encodedPubKey);

		    return result;

		} catch (NoSuchAlgorithmException e) {
		    throw new IdentityException("Failed to create identity", e);
		}
	    }
	});
    }

    @Override
    public NodeIdentity<?> issueIdentity(IssueIdentityRequest request) {
	Class<? extends IdentityType> clazz = request.getIdentityType().getClass();
	IdentityBuilder<?> builder = identityBuilders.get(clazz);
	if (builder == null) {
	    throw new RuntimeException("No identity builder found for type " + clazz);
	}
	return builder.buildIdentity(request);
    }

    public IdentitiesRepository getIdentitiesRepository() {
	return identitiesRepository;
    }

    public void setIdentitiesRepository(IdentitiesRepository identitiesRepository) {
	this.identitiesRepository = identitiesRepository;
    }

    @Override
    public void storeIdentityProfile(NodeIdentityProfile identityProfile) {
	identitiesRepository.storeIdentityProfile(identityProfile);
    }

    @Override
    public NodeIdentityProfile getIdentityProfile(String identityKey) {
	return identitiesRepository.getIdentityProfile(identityKey);
    }

}
