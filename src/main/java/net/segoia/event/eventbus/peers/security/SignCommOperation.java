package net.segoia.event.eventbus.peers.security;

public class SignCommOperation implements CommOperation<SpkiSpkiCommOperationContext<SignCommOperationContext>, SignCommOperationOutput>{

    @Override
    public SignCommOperationOutput operate(SpkiSpkiCommOperationContext<SignCommOperationContext> context) throws CommOperationException {
	SpkiPrivateIdentityData ourIdentity = context.getOurIdentity();
	SignCommOperationContext opContex = context.getOpContex();
	byte[] signature;
	try {
	    signature = ourIdentity.sign(opContex);
	} catch (Exception e) {
	    throw new CommOperationException("Signature failed",e,context);
	}
	
	return new SignCommOperationOutput(opContex.getData(), signature);
    }

}
