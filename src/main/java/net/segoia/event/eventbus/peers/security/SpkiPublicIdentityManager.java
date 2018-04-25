package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;
import net.segoia.event.eventbus.peers.events.auth.id.SpkiNodeIdentity;

public interface SpkiPublicIdentityManager extends PublicIdentityManager<SpkiNodeIdentity>{
//    public byte[] ecryptPublic(EncryptOperationContext context) throws Exception;
//    public boolean verifySignature(VerifySignatureOperationContext context) throws Exception;
    
    public EncryptOperationWorker buildEncryptPublicWorker(EncryptWithPublicCommOperationDef opDef) throws Exception;
    public VerifySignatureOperationWorker buildVerifySignatureWorker(SignCommOperationDef opDef) throws Exception;
}
