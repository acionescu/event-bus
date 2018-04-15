package net.segoia.event.eventbus.peers.security;

import java.util.List;

public class SequentialOperationsProcessor {
    private List<OperationExecutionContext> opContexts;

    public SequentialOperationsProcessor(List<OperationExecutionContext> opContexts) {
	super();
	this.opContexts = opContexts;
    }

    public CommDataContext processs(CommDataContext context) throws GenericOperationException {
//	byte[] data = context.getData();
//	OperationData operationData = new OperationData(data);
//	OperationOutput output=null;
	for (OperationExecutionContext oc : opContexts) {
	    
//	    output = oc.executeOperation(operationData);
//	    operationData = output;
	    
	    context.processOperation(oc);
	}
//	return new CommDataContext(output.getFullData());
	return context;
    }

}
