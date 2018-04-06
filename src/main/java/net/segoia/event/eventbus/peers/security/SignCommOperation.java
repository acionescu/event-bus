package net.segoia.event.eventbus.peers.security;

public class SignCommOperation implements CommOperation<SpkiSpkiCommOperationContext<SignCommOperationContext>, SignCommOperationOutput>{

    @Override
    public SignCommOperationOutput operate(SpkiSpkiCommOperationContext<SignCommOperationContext> context) {
	SpkiPrivateIdentityData ourIdentity = context.getOurIdentity();
	SignCommOperationContext opContex = context.getOpContex();
	byte[] signature = ourIdentity.sign(opContex);
	
	return new SignCommOperationOutput(opContex.getData(), signature);
    }

}
