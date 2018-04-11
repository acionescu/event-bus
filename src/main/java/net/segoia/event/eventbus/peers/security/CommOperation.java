package net.segoia.event.eventbus.peers.security;

public interface CommOperation<I extends OperationData, C extends OperationContext<?>, O extends OperationOutput>
	extends GenericOperation<I, C, O> {
}
