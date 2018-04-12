package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;

public class SignCommOperationContext extends SpkiSpkiCommOperationContext<SignCommOperationDef> {
    private SignOperationWorker signWorker;

    public SignCommOperationContext(SignCommOperationDef opDef, SpkiPrivateIdentityManager ourIdentity,
	    SpkiPublicIdentityManager peerIdentity) {
	super(opDef, ourIdentity, peerIdentity);
    }

    protected SignOperationWorker getSignWorker() throws Exception {
	if(signWorker== null) {
	    signWorker=getOurIdentity().buildSignWorker(getOpDef());
	}
        return signWorker;
    }

    public SignCommOperationOutput sign(byte[] data) throws Exception {
	byte[] signature = getSignWorker().sign(data);
	return new SignCommOperationOutput(data, signature);
    }
    
}
