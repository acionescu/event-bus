package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.SignCommOperationDef;

public class VerifySignatureOperationContext extends SpkiSpkiCommOperationContext<SignCommOperationDef> {
    private VerifySignatureOperationWorker verifyWorker;

    public VerifySignatureOperationContext(SignCommOperationDef opDef, SpkiPrivateIdentityManager ourIdentity,
	    SpkiPublicIdentityManager peerIdentity) {
	super(opDef, ourIdentity, peerIdentity);
    }

    protected VerifySignatureOperationWorker getVerifyWorker() throws Exception{
	if(verifyWorker==null) {
	    verifyWorker = getPeerIdentity().buildVerifySignatureWorker(getOpDef());
	}
        return verifyWorker;
    }
    
    public boolean verify(SignCommOperationOutput sigData) throws Exception{
	return getVerifyWorker().verify(sigData.getData(), sigData.getSignature());
    }

}
