package net.segoia.event.eventbus.peers.security;

public interface GenericOperation<I extends OperationData, C extends OperationContext<?>, O extends OperationOutput> {
    O operate(OperationDataContext<I, C> dataContext) throws GenericOperationException;
}
