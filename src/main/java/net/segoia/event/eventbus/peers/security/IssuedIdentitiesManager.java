package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;

public interface IssuedIdentitiesManager {
    boolean verify(NodeIdentity<?> identity);
    void storeIdentity(NodeIdentity<?> identity);
}
