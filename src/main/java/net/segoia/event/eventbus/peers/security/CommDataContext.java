package net.segoia.event.eventbus.peers.security;

public class CommDataContext {
    private byte[] data;
    private OperationExecutionAccumulator acc;

    public CommDataContext() {
	super();
    }

    public CommDataContext(byte[] data) {
	super();
	this.data = data;
    }
    
    public CommDataContext(OperationExecutionAccumulator acc) {
	super();
	this.acc = acc;
    }

    public void processOperation(OperationExecutionContext oec) throws GenericOperationException {
	if(acc == null) {
	    acc = new OperationExecutionAccumulator(new OperationData(data));
	}
	acc.processOperation(oec);
    }

    public byte[] getData() {
	return data;
    }

    public void setData(byte[] data) {
	this.data = data;
    }
    
    public byte[] getResult() {
	return acc.getResult().getFullData();
    }

}
