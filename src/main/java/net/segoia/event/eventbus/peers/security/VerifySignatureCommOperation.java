package net.segoia.event.eventbus.peers.security;

public class VerifySignatureCommOperation implements CommOperation<SpkiSpkiCommOperationContext<VerifySignatureOperationContext>, OperationOutput>{

    @Override
    public OperationOutput operate(SpkiSpkiCommOperationContext<VerifySignatureOperationContext> context)
	    throws CommOperationException {
	SpkiPublicIdentityManager peerIdentity = context.getPeerIdentity();
	VerifySignatureOperationContext opContex = context.getOpContex();
	boolean isValid = peerIdentity.verifySignature(opContex);
	if(!isValid) {
	    throw new SignatureInvalidException(opContex, peerIdentity);
	}
	return new OperationOutput(opContex.getSignOutput().getData());
	
    }

}
