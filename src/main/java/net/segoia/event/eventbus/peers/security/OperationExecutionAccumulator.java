package net.segoia.event.eventbus.peers.security;

import java.util.ArrayList;
import java.util.List;

public class OperationExecutionAccumulator {
    protected OperationData currentData;
    private List<OperationOutput> outputs;
    protected OperationOutput lastOutput;
    
    public OperationExecutionAccumulator(OperationData currentData) {
	super();
	this.currentData = currentData;
	outputs = new ArrayList<>();
    }
    
    public void processOperation(OperationExecutionContext oec) throws GenericOperationException {
	lastOutput = oec.executeOperation(currentData);
	outputs.add(lastOutput);
	processLastOutput();
    }
    
    
    protected void processLastOutput() {
	currentData = lastOutput;
    }

    public List<OperationOutput> getOutputs() {
        return outputs;
    }
    
    public OperationOutput getResult() {
	return lastOutput;
    }

    public OperationData getCurrentData() {
        return currentData;
    }
    
}
