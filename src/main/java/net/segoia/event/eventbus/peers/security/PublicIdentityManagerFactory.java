package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentityType;

public interface PublicIdentityManagerFactory<N extends NodeIdentity<? extends NodeIdentityType>> {
    
    PublicIdentityManager build(N nodeIdentity);
}
