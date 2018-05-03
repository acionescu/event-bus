package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.auth.id.SharedNodeIdentity;

public interface SessionManagerFactory<T extends SessionManager> {
    T build(SharedNodeIdentity identity);
}
