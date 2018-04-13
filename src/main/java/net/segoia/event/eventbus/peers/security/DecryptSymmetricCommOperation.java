package net.segoia.event.eventbus.peers.security;

public class DecryptSymmetricCommOperation implements CommOperation<OperationData, DecryptSymmetricCommOperationContext, OperationOutput>{

    @Override
    public OperationOutput operate(
	    OperationDataContext<OperationData, DecryptSymmetricCommOperationContext> dataContext)
	    throws GenericOperationException {
	DecryptSymmetricCommOperationContext context = dataContext.getOpContext();
	
	try {
	    byte[] decryptedData = context.decrypt(dataContext.getInputData().getFullData());
	    return new OperationOutput(decryptedData);
	} catch (Exception e) {
	    throw new CommOperationException("Failed to decrypt data", e, context);
	}
    }

}
