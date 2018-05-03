package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.core.PublicIdentityManager;
import net.segoia.event.eventbus.peers.vo.auth.id.SpkiNodeIdentity;
import net.segoia.event.eventbus.peers.vo.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.vo.comm.SignCommOperationDef;

public interface SpkiPublicIdentityManager extends PublicIdentityManager<SpkiNodeIdentity>{
//    public byte[] ecryptPublic(EncryptOperationContext context) throws Exception;
//    public boolean verifySignature(VerifySignatureOperationContext context) throws Exception;
    
    public EncryptOperationWorker buildEncryptPublicWorker(EncryptWithPublicCommOperationDef opDef) throws Exception;
    public VerifySignatureOperationWorker buildVerifySignatureWorker(SignCommOperationDef opDef) throws Exception;
}
