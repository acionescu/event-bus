package net.segoia.event.eventbus.peers.security;

import java.util.ArrayList;
import java.util.List;

import net.segoia.event.eventbus.peers.comm.CommOperationDef;

public class SpkiSpkiCommManager extends AbstractCommManager<SpkiPrivateIdentityData, SpkiPublicIdentityManager> {
    private CommManagerConfig config;
    
    private SequentialOperationsProcessor txOpProcessor;
    private SequentialOperationsProcessor rxOpProcessor;
    
    public void init() {
	
	/* build sequential processor for tx operations */
	List<CommOperationDef> txOpDef = getTxStrategy().getDirectTxStrategy().getOperations();
	
	List<OperationExecutionContext> txOpExecContexts = new ArrayList<>();
	
	for(int i=0;i< txOpDef.size();i++) {
	    CommOperationDef cDef = txOpDef.get(i);
	    OperationExecutionContext oec = new OperationExecutionContext(config.getTxOperation(cDef), config.getTxOpContextBuilder(cDef).buildContext(cDef, getOurIdentity(), getPeerIdentity()));
	    
	    txOpExecContexts.add(oec);
	}
	
	txOpProcessor = new SequentialOperationsProcessor(txOpExecContexts);
	
	/* build sequential processor for rx operations */
	List<CommOperationDef> rxOpDef = getRxStrategy().getDirectTxStrategy().getOperations();
	List<OperationExecutionContext> rxOpExecContexts = new ArrayList<>();
	
	for(int i=0;i< rxOpDef.size();i++) {
	    CommOperationDef cDef = rxOpDef.get(i);
	    OperationExecutionContext oec = new OperationExecutionContext(config.getRxOperation(cDef), config.getRxOpContextBuilder(cDef).buildContext(cDef, getOurIdentity(), getPeerIdentity()));
	    
	    rxOpExecContexts.add(oec);
	}
	
	rxOpProcessor = new SequentialOperationsProcessor(rxOpExecContexts);
	
    }

    @Override
    public CommDataContext processsOutgoingData(CommDataContext context) throws CommOperationException {
//	List<CommOperationDef> operations = getTxStrategy().getDirectTxStrategy().getOperations();
//
//	byte[] data = context.getData();
//
//	OperationOutput currentContext = new OperationOutput(data);
//
//	for (CommOperationDef oDef : operations) {
//	    CommOperation op = config.getTxOperation(oDef);
//	    currentContext = op.operate(buildContext(oDef, currentContext));
//	}
//	return new CommDataContext(currentContext.getFullData());
	
	return null;
    }

    @Override
    public CommDataContext processIncomingData(CommDataContext context) {
	// TODO Auto-generated method stub
	return null;
    }

//    protected SpkiSpkiCommOperationContext<OperationContext> buildContext(CommOperationDef def,
//	    OperationOutput inputContext) {
//	OperationContext opContext = config.getTxOpContextBuilder(def).buildContext(def, inputContext);
//	return new SpkiSpkiCommOperationContext<>(opContext, getOurIdentity(), getPeerIdentity());
//    }

    public CommManagerConfig getConfig() {
	return config;
    }

    public void setConfig(CommManagerConfig config) {
	this.config = config;
    }

}
