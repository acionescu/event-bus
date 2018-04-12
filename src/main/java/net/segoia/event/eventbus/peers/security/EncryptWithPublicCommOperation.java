package net.segoia.event.eventbus.peers.security;

public class EncryptWithPublicCommOperation
	implements CommOperation<OperationData, EncryptWithPrivateOperationContext, OperationOutput> {

    @Override
    public OperationOutput operate(OperationDataContext<OperationData, EncryptWithPrivateOperationContext> dataContext)
	    throws GenericOperationException {
	EncryptWithPrivateOperationContext context = dataContext.getOpContext();
	try {
	    byte[] encryptedData = context.encrypt(dataContext.getInputData().getFullData());
	    return new OperationOutput(encryptedData);
	} catch (Throwable e) {
	    throw new CommOperationException("Faield to encrypt data", e, context);
	}
    }

}
