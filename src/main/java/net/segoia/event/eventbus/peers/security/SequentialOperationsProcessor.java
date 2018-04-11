package net.segoia.event.eventbus.peers.security;

import java.util.List;

public class SequentialOperationsProcessor {
    private List<OperationExecutionContext<?, ?, ?, ?>> opContexts;

    public SequentialOperationsProcessor(List<OperationExecutionContext<?, ?, ?, ?>> opContexts) {
	super();
	this.opContexts = opContexts;
    }

    public CommDataContext processs(CommDataContext context) throws CommOperationException {
	byte[] data = context.getData();

	for (OperationExecutionContext oc : opContexts) {
	    OperationData operationData = new OperationData(data);
	    OperationOutput output = oc.executeOperation(operationData);
	    data = output.getFullOutputData();
	}
	return new CommDataContext(data);
    }

}
