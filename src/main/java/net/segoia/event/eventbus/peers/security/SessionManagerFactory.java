package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.SharedNodeIdentity;

public interface SessionManagerFactory<T extends SessionManager> {
    T build(SharedNodeIdentity identity);
}
