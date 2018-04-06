package net.segoia.event.eventbus.peers.security;

public interface CommOperation<C extends CommOperationContext<?, ?, ?>, O extends OperationOutput> {
    O operate(C context) throws CommOperationException;
}
