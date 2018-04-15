package net.segoia.event.eventbus.peers.security;

import java.util.HashMap;
import java.util.Map;

public class EncryptWithPublicCommOperation
	implements CommOperation<OperationData, EncryptWithPublicOperationContext, OperationOutput> {

    private Map<Class<?>, CommOperation> handlersByClass = new HashMap<>();

    public EncryptWithPublicCommOperation() {
	super();
//	handlersByClass.put(SignCommOperationOutput.class, new CommOperation<SignCommOperationOutput, EncryptWithPublicOperationContext, OperationOutput>() {
//
//	    @Override
//	    public OperationOutput operate(
//		    OperationDataContext<SignCommOperationOutput, EncryptWithPublicOperationContext> dataContext)
//		    throws GenericOperationException {
//		EncryptWithPublicOperationContext context = dataContext.getOpContext();
//		SignCommOperationOutput inputData = dataContext.getInputData();
//		 byte[] encryptedData = context.encrypt(inputData.getFullData());
//		    return new OperationOutput(encryptedData);
//	    }
//	});
    }

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
