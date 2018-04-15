package net.segoia.event.eventbus.peers.security;

public class VerifySignatureCommOperation
	implements CommOperation<SignCommOperationOutput, VerifySignatureOperationContext, OperationOutput> {

    @Override
    public OperationOutput operate(
	    OperationDataContext<SignCommOperationOutput, VerifySignatureOperationContext> dataContext)
	    throws GenericOperationException {
	VerifySignatureOperationContext context = dataContext.getOpContext();

	boolean isValid;
	SignCommOperationOutput sigData = null;
	try {
	    sigData = context.deserializeTo(SignCommOperationOutput.class, dataContext.getInputData());
	    isValid = context.verify(sigData);

	} catch (Throwable e) {
	    throw new CommOperationException("Failed to verify signature", e, context);
	}
	if (!isValid) {
	    throw new SignatureInvalidException(context, context.getPeerIdentity());
	}
	
	return new OperationOutput(sigData.getData());
    }

}
