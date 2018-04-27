package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.services.NodeIdentityProfile;

public interface IdentitiesManager {
    NodeIdentity<?> issueIdentity(IssueIdentityRequest request);
    void storeIdentityProfile(NodeIdentityProfile identityProfile);
    NodeIdentityProfile getIdentityProfile(String identityKey);
}
