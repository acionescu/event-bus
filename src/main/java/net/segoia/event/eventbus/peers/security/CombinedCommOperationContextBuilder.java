package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.CommOperationDef;

public interface CombinedCommOperationContextBuilder<D extends CommOperationDef> extends CommOperationContextBuilder<D, CombinedCommProtocolContext>{

}