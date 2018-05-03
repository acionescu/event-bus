package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.CommOperationDef;

public interface CommOperationContextBuilder<D extends CommOperationDef, C extends CommProtocolContext> {
    OperationContext<D> buildContext(D def, C context);
}
