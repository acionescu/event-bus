package net.segoia.event.eventbus.peers.security;

public class EncryptSymmetricCommOperation implements CommOperation<OperationData, EncryptSymmetricCommOperationContext, OperationOutput>{

    @Override
    public OperationOutput operate(
	    OperationDataContext<OperationData, EncryptSymmetricCommOperationContext> dataContext)
	    throws GenericOperationException {
	EncryptSymmetricCommOperationContext context = dataContext.getOpContext();
	
	try {
	    byte[] encryptedData = context.encrypt(dataContext.getInputData().getFullData());
	    return new OperationOutput(encryptedData);
	} catch (Exception e) {
	    throw new CommOperationException("Faield to encrypt data", e, context);
	}
    }

}
