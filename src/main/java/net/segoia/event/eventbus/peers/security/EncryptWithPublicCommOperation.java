package net.segoia.event.eventbus.peers.security;

public class EncryptWithPublicCommOperation
	implements CommOperation<SpkiSpkiCommOperationContext<EncryptOperationContext>, OperationOutput> {

    @Override
    public OperationOutput operate(SpkiSpkiCommOperationContext<EncryptOperationContext> context)
	    throws CommOperationException {

	SpkiPublicIdentityManager peerIdentity = context.getPeerIdentity();
	byte[] encryptedData;
	try {
	    encryptedData = peerIdentity.ecryptPublic(context.getOpContex());
	    return new OperationOutput(encryptedData);
	} catch (Exception e) {
	    throw new CommOperationException("Faield to encrypt data", e, context);
	}

    }

}
