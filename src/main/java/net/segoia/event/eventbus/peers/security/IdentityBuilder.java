package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;

public interface IdentityBuilder<I extends NodeIdentity<?>> {
    I buildIdentity(IssueIdentityRequest request) throws IdentityException;
}
