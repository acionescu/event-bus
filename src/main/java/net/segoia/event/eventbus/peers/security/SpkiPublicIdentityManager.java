package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;

public interface SpkiPublicIdentityManager extends PublicIdentityManager{
//    public byte[] ecryptPublic(EncryptOperationContext context) throws Exception;
//    public boolean verifySignature(VerifySignatureOperationContext context) throws Exception;
    
    public EncryptOperationWorker buildEncryptPublicWorker(EncryptWithPublicCommOperationDef opDef) throws Exception;
    public VerifySignatureOperationWorker buildVerifySignatureWorker(SignCommOperationDef opDef) throws Exception;
}
