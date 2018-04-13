package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.IdentityType;

public interface PublicIdentityManagerFactory<N extends NodeIdentity<? extends IdentityType>> {
    
    PublicIdentityManager build(N nodeIdentity);
}
