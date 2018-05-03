package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.EncryptSymmetricOperationDef;

public interface SharedSecretIdentityManager {
    public EncryptOperationWorker buildEncryptWorker(EncryptSymmetricOperationDef opDef) throws Exception;
    public DecryptOperationWorker buildDecryptWorker(EncryptSymmetricOperationDef opDef) throws Exception;
    
    
}
