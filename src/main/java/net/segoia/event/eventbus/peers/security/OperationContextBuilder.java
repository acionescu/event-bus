package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.CommOperationDef;

public interface OperationContextBuilder<D extends CommOperationDef> {
    OperationContext buildContext(D def, OperationOutput opContext);
}
