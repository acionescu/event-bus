package net.segoia.event.eventbus.peers.security;

import java.util.List;

import net.segoia.event.eventbus.peers.comm.CommOperationDef;

public class SpkiSpkiCommManager extends AbstractCommManager<SpkiPrivateIdentityData, SpkiPublicIdentityManager> {
    private CommManagerConfig config;

    @Override
    public CommDataContext processsOutgoingData(CommDataContext context) throws CommOperationException {
	List<CommOperationDef> operations = getTxStrategy().getDirectTxStrategy().getOperations();

	byte[] data = context.getData();

	OperationOutput currentContext = new OperationOutput(data);

	for (CommOperationDef oDef : operations) {
	    CommOperation op = config.getTxOperation(oDef);
	    currentContext = op.operate(buildContext(oDef, currentContext));
	}
	return new CommDataContext(currentContext.getFullOutputData());
    }

    @Override
    public CommDataContext processIncomingData(CommDataContext context) {
	// TODO Auto-generated method stub
	return null;
    }

    protected SpkiSpkiCommOperationContext<OperationContext> buildContext(CommOperationDef def,
	    OperationOutput inputContext) {
	OperationContext opContext = config.getTxOpContextBuilder(def).buildContext(def, inputContext);
	return new SpkiSpkiCommOperationContext<>(opContext, getOurIdentity(), getPeerIdentity());
    }

    public CommManagerConfig getConfig() {
	return config;
    }

    public void setConfig(CommManagerConfig config) {
	this.config = config;
    }

}
