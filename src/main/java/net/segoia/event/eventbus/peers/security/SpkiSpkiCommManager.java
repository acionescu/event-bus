package net.segoia.event.eventbus.peers.security;

import java.nio.charset.Charset;
import java.util.List;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.peers.comm.CommOperationDef;

public class SpkiSpkiCommManager extends CommManager<SpkiPrivateIdentityData, SpkiPublicIdentityManager> {
    private CommManagerConfig config;

    @Override
    public Event processsOutgoingEvent(EventContext eventContext) throws CommOperationException {
	List<CommOperationDef> operations = getTxStrategy().getDirectTxStrategy().getOperations();

	byte[] data = eventContext.getEvent().toJson().getBytes(Charset.forName("UTF-8"));

	OperationOutput currentContext = new OperationOutput(data);

	for (CommOperationDef oDef : operations) {
	    CommOperation op = config.getTxOperation(oDef);
	    currentContext = op.operate(buildContext(oDef, currentContext));
	}
	return opContextToEvent(currentContext);
    }

    @Override
    public Event processIncomingEvent(EventContext context) {
	// TODO Auto-generated method stub
	return null;
    }

    protected Event opContextToEvent(OperationContext opContext) {
	return null;
    }

    protected SpkiSpkiCommOperationContext<OperationContext> buildContext(CommOperationDef def,
	    OperationOutput inputContext) {
	OperationContext opContext = config.getTxOpContextBuilder(def).buildContext(def, inputContext);
	return new SpkiSpkiCommOperationContext<>(opContext, getOurIdentity(), getPeerIdentity());
    }

}
