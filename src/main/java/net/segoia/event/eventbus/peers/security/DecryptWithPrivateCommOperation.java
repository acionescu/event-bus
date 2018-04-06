package net.segoia.event.eventbus.peers.security;

public class DecryptWithPrivateCommOperation
	implements CommOperation<SpkiSpkiCommOperationContext<DecryptOperationContext>, OperationOutput> {

    @Override
    public OperationOutput operate(SpkiSpkiCommOperationContext<DecryptOperationContext> context)
	    throws CommOperationException {
	SpkiPrivateIdentityData ourIdentity = context.getOurIdentity();

	byte[] decryptedData = ourIdentity.decryptPrivate(context.getOpContex());
	return new OperationOutput(decryptedData);
    }

}
