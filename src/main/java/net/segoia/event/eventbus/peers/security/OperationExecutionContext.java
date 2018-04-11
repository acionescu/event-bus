package net.segoia.event.eventbus.peers.security;

public class OperationExecutionContext<I extends OperationData, D extends OperationDef, C extends OperationContext<D>, O extends OperationOutput> {
    private GenericOperation<I,D,C,O> operation;
    private C opContext;
    
    public O executeOperation(I operationData) {
	return operation.operate(new OperationDataContext<>(operationData, opContext));
    }
}
