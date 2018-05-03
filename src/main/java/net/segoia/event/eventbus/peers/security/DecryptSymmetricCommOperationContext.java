package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.EncryptSymmetricOperationDef;

public class DecryptSymmetricCommOperationContext extends SymmetricCommOperationContext<EncryptSymmetricOperationDef>{
    public DecryptSymmetricCommOperationContext(EncryptSymmetricOperationDef opDef,
	    SharedSecretIdentityManager identityManager) {
	super(opDef, identityManager);
    }



    private DecryptOperationWorker opWorker;

    

    public DecryptOperationWorker getOpWorker() throws Exception {
	if(opWorker==null) {
	    opWorker = getIdentityManager().buildDecryptWorker(getOpDef());
	}
        return opWorker;
    }
    
    public byte[] decrypt(byte[] data) throws Exception {
	return getOpWorker().decrypt(data);
    }
    
}
