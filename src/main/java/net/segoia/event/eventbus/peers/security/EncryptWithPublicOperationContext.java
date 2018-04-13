package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.EncryptWithPublicCommOperationDef;

public class EncryptWithPublicOperationContext extends SpkiSpkiCommOperationContext<EncryptWithPublicCommOperationDef> {
    private EncryptOperationWorker encryptWorker;

    public EncryptWithPublicOperationContext(EncryptWithPublicCommOperationDef opDef, SpkiPrivateIdentityManager ourIdentity,
	    SpkiPublicIdentityManager peerIdentity) {
	super(opDef, ourIdentity, peerIdentity);
    }

    public EncryptOperationWorker getEncryptWorker() throws Exception{
	if(encryptWorker==null) {
	    encryptWorker = getPeerIdentity().buildEncryptPublicWorker(getOpDef());
	}
        return encryptWorker;
    }
    
    public byte[] encrypt(byte[] data) throws Exception{
	return getEncryptWorker().encrypt(data);
    }

}
