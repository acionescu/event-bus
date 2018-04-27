package net.segoia.event.eventbus.peers.security;

import java.util.HashMap;
import java.util.Map;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.services.NodeIdentityProfile;

public class InMemoryIdentitiesRepository implements IdentitiesRepository {
    private Map<Class<?>, IssuedIdentityManager<?>> identityManagers;
    private Map<String, NodeIdentityProfile> identityProfiles=new HashMap<>();

    public InMemoryIdentitiesRepository() {
	identityManagers = new HashMap<>();
	identityManagers.put(SpkiNodeIdentity.class, new IssuedIdentityManager<SpkiNodeIdentity>() {
	    private Map<String, NodeIdentityCertificationInfo> issuedIdentities = new HashMap<>();

	    @Override
	    public boolean verify(SpkiNodeIdentity identity) {
		String publicKey = identity.getPublicKey();
		return issuedIdentities.get(publicKey) != null;
	    }

	    @Override
	    public void store(SpkiNodeIdentity identity) {
		issuedIdentities.put(identity.getPublicKey(), new NodeIdentityCertificationInfo());
	    }
	});
    }

    public interface IssuedIdentityManager<I extends NodeIdentity<?>> {
	boolean verify(I identity);

	void store(I identity);
    }

    @Override
    public void storeIdentityProfile(NodeIdentityProfile identityProfile) {
	identityProfiles.put(identityProfile.getIdentityKey(), identityProfile);
    }

    @Override
    public NodeIdentityProfile getIdentityProfile(String identityKey) {
	return identityProfiles.get(identityKey);
    }

//    @Override
//    public boolean verify(NodeIdentity<?> identity) {
//	IssuedIdentityManager im = identityManagers.get(identity.getClass());
//	if (im != null) {
//	    return im.verify(identity);
//	}
//	return true;
//    }
//
//    @Override
//    public void storeIdentity(NodeIdentity<?> identity) {
//	IssuedIdentityManager im = identityManagers.get(identity.getClass());
//	if (im != null) {
//	    im.store(identity);
//	} else {
//	    throw new RuntimeException("Node identity manager found for type " + identity.getClass());
//	}
//    }
}
