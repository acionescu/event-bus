package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;

public interface IdentitiesManager {
    NodeIdentity<?> issueIdentity(IssueIdentityRequest request);
}
