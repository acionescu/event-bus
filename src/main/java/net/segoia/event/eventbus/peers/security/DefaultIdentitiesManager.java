package net.segoia.event.eventbus.peers.security;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import net.segoia.event.eventbus.peers.events.auth.id.IdentityType;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiFullIdentityType;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiFullNodeIdentity;
import net.segoia.event.eventbus.peers.events.session.KeyDef;
import net.segoia.util.crypto.CryptoUtil;

public class DefaultIdentitiesManager implements IdentitiesManager {
    private Map<Class<?>, IdentityBuilder<?>> identityBuilders;
    private IssuedIdentitiesManager issuedIdentitiesManager= new InMemoryIssuedIdentitiesManager();

    public DefaultIdentitiesManager() {
	identityBuilders = new HashMap<>();
	identityBuilders.put(SpkiFullIdentityType.class, new IdentityBuilder<SpkiFullNodeIdentity>() {

	    @Override
	    public SpkiFullNodeIdentity buildIdentity(IssueIdentityRequest request) {
		SpkiFullIdentityType identityType = (SpkiFullIdentityType) request.getIdentityType();
		KeyDef keyDef = identityType.getKeyDef();
		try {
		    KeyPair keyPair = CryptoUtil.generateKeyPair(keyDef.getAlgorithm(), keyDef.getKeySize());
		    String encodedPubKey = CryptoUtil.base64Encode(keyPair.getPublic().getEncoded());
		    String encodedPrivateKey = CryptoUtil.base64Encode(keyPair.getPrivate().getEncoded());

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

    public IssuedIdentitiesManager getIssuedIdentitiesManager() {
        return issuedIdentitiesManager;
    }

    public void setIssuedIdentitiesManager(IssuedIdentitiesManager issuedIdentitiesManager) {
        this.issuedIdentitiesManager = issuedIdentitiesManager;
    }
    
    

}
