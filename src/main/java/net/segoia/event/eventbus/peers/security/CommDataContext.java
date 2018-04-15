package net.segoia.event.eventbus.peers.security;

public class CommDataContext {
    private OperationExecutionAccumulator acc;


    public CommDataContext(byte[] data) {
	super();
	acc = new OperationExecutionAccumulator(new OperationData(data));
    }
    
    public CommDataContext(OperationData opData) {
	super();
	this.acc = new OperationExecutionAccumulator(opData);
    }

    public CommDataContext(OperationExecutionAccumulator acc) {
	super();
	this.acc = acc;
    }

    public void processOperation(OperationExecutionContext oec) throws GenericOperationException {
	
	acc.processOperation(oec);
    }

    public byte[] getData() {
	return acc.getCurrentData().getData();
    }

    public OperationOutput getResult() {
	return acc.getResult();
    }

}
