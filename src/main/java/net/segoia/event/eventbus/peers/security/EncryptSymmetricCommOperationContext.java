package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.EncryptSymmetricOperationDef;

public class EncryptSymmetricCommOperationContext extends SymmetricCommOperationContext<EncryptSymmetricOperationDef>{
    private EncryptOperationWorker opWorker;
    
    public EncryptSymmetricCommOperationContext(EncryptSymmetricOperationDef opDef,
	    SharedSecretIdentityManager identityManager) {
	super(opDef, identityManager);
    }

    public EncryptOperationWorker getOpWorker() throws Exception {
	if(opWorker == null) {
	    opWorker = getIdentityManager().buildEncryptWorker(getOpDef());
	}
        return opWorker;
    }
    
    public byte[] encrypt(byte[] data) throws Exception{
   	return getOpWorker().encrypt(data);
       }
    
    

}
