package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.services.NodeIdentityProfile;

public interface IdentitiesRepository {
    void storeIdentityProfile(NodeIdentityProfile identityProfile);
    NodeIdentityProfile getIdentityProfile(String identityKey);
}
