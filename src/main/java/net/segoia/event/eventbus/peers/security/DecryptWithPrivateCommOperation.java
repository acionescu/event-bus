package net.segoia.event.eventbus.peers.security;

public class DecryptWithPrivateCommOperation
	implements CommOperation<OperationData, DecryptWithPrivateOperationContext, OperationOutput> {

    @Override
    public OperationOutput operate(
	    OperationDataContext<OperationData, DecryptWithPrivateOperationContext> dataContext) throws CommOperationException {
	DecryptWithPrivateOperationContext context = dataContext.getOpContext();

	byte[] decryptedData;
	try {
	    decryptedData = context.decrypt(dataContext.getInputData().getFullData());
	    return new OperationOutput(decryptedData);
	} catch (Throwable e) {
	    throw new CommOperationException("Failed to decrypt data", e, context);
	}
    }

//    @Override
//    public OperationOutput operate(SpkiSpkiCommOperationContext<DecryptOperationContext> context)
//	    throws CommOperationException {
//	SpkiPrivateIdentityData ourIdentity = context.getOurIdentity();
//
//	byte[] decryptedData;
//	try {
//	    decryptedData = ourIdentity.decryptPrivate(context.getOpContex());
//	    return new OperationOutput(decryptedData);
//	} catch (Exception e) {
//	    throw new CommOperationException("Failed to decrypt data", e, context);
//	}
//
//    }

    
}
