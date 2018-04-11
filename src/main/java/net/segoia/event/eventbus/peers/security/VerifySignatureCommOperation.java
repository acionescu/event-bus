package net.segoia.event.eventbus.peers.security;

public class VerifySignatureCommOperation
	implements CommOperation<SpkiSpkiCommOperationContext<VerifySignatureOperationContext>, OperationOutput> {

    @Override
    public OperationOutput operate(SpkiSpkiCommOperationContext<VerifySignatureOperationContext> context)
	    throws CommOperationException {
	SpkiPublicIdentityManager peerIdentity = context.getPeerIdentity();
	VerifySignatureOperationContext opContex = context.getOpContex();
	boolean isValid;
	try {
	    isValid = peerIdentity.verifySignature(opContex);
	} catch (Exception e) {
	    throw new CommOperationException("Failed to verify signature", e, context);
	}
	if (!isValid) {
	    throw new SignatureInvalidException(opContex, peerIdentity);
	}
	return new OperationOutput(opContex.getSignOutput().getData());

    }

}
