package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.CommOperationDef;

public interface SpkiOperationContextBuilder<D extends CommOperationDef> {
    OperationContext<D> buildContext(D def, SpkiPrivateIdentityManager ourIdentity, SpkiPublicIdentityManager peerIdentity);
}
