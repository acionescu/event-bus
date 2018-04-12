package net.segoia.event.eventbus.peers.security;

public class OperationExecutionContext<I extends OperationData, C extends OperationContext<?>, O extends OperationOutput> {
    private GenericOperation<I, C, O> operation;
    private C opContext;

    public OperationExecutionContext(GenericOperation<I, C, O> operation, C opContext) {
	super();
	this.operation = operation;
	this.opContext = opContext;
    }

    public O executeOperation(I operationData) throws GenericOperationException {
	return operation.operate(new OperationDataContext<>(operationData, opContext));
    }
}
