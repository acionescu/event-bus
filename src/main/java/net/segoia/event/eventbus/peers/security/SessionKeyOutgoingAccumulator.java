package net.segoia.event.eventbus.peers.security;

public class SessionKeyOutgoingAccumulator extends OperationExecutionAccumulator{

    public SessionKeyOutgoingAccumulator(OperationData currentData) {
	super(currentData);
    }

    @Override
    protected void processLastOutput() {
	currentData = new OperationData(lastOutput.getData());
    }
    
    

}
