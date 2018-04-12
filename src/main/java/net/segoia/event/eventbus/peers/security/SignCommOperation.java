package net.segoia.event.eventbus.peers.security;

public class SignCommOperation
	implements CommOperation<OperationData, SignCommOperationContext, SignCommOperationOutput> {

    @Override
    public SignCommOperationOutput operate(OperationDataContext<OperationData, SignCommOperationContext> dataContext)
	    throws GenericOperationException {

	SignCommOperationContext context = dataContext.getOpContext();
	try {
	    return context.sign(dataContext.getInputData().getFullData());
	} catch (Throwable e) {
	    throw new CommOperationException("Signature failed", e, context);
	}
    }

}
