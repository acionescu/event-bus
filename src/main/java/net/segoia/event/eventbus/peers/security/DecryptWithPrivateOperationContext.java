package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.EncryptWithPublicCommOperationDef;

public class DecryptWithPrivateOperationContext extends SpkiSpkiCommOperationContext<EncryptWithPublicCommOperationDef> {
    private DecryptOperationWorker privateDecryptWorker;

    public DecryptWithPrivateOperationContext(EncryptWithPublicCommOperationDef opDef, SpkiPrivateIdentityManager ourIdentity,
	    SpkiPublicIdentityManager peerIdentity) {
	super(opDef, ourIdentity, peerIdentity);
    }

    protected DecryptOperationWorker getPrivateDecryptWorker() throws Exception {
	if (privateDecryptWorker == null) {
	    privateDecryptWorker = getOurIdentity().buildPrivateDecryptWorker(getOpDef());
	}
	return privateDecryptWorker;
    }

    public byte[] decrypt(byte[] data) throws Exception {
	try {
	    return getPrivateDecryptWorker().decrypt(data);
	} catch (Throwable t) {
	    /* in case of error, force reset of the worker */
	    privateDecryptWorker = null;
	    throw t;
	}
    }

}
