package net.segoia.event.eventbus.peers.security;

public class OperationDataContext<I extends OperationData, C extends OperationContext<?>> {
    private I inputData;
    private C opContext;

    public OperationDataContext(I inputData, C opContext) {
	super();
	this.inputData = inputData;
	this.opContext = opContext;
    }

    public I getInputData() {
	return inputData;
    }

    public C getOpContext() {
	return opContext;
    }

}
