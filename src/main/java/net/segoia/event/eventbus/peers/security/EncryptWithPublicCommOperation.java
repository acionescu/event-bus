package net.segoia.event.eventbus.peers.security;

public class EncryptWithPublicCommOperation
	implements CommOperation<SpkiSpkiCommOperationContext<EncryptOperationContext>, OperationOutput> {

    @Override
    public OperationOutput operate(SpkiSpkiCommOperationContext<EncryptOperationContext> context) {
	
	SpkiPublicIdentityManager peerIdentity = context.getPeerIdentity();
	byte[] encryptedData = peerIdentity.ecryptPublic(context.getOpContex());
	return new OperationOutput(encryptedData);
    }

}
