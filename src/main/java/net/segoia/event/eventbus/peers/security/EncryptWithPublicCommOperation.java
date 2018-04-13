package net.segoia.event.eventbus.peers.security;

public class EncryptWithPublicCommOperation
	implements CommOperation<OperationData, EncryptWithPublicOperationContext, OperationOutput> {

    @Override
    public OperationOutput operate(OperationDataContext<OperationData, EncryptWithPublicOperationContext> dataContext)
	    throws GenericOperationException {
	EncryptWithPublicOperationContext context = dataContext.getOpContext();
	try {
	    byte[] encryptedData = context.encrypt(dataContext.getInputData().getFullData());
	    return new OperationOutput(encryptedData);
	} catch (Throwable e) {
	    throw new CommOperationException("Faield to encrypt data", e, context);
	}
    }

}
