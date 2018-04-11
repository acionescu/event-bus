package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.EncryptCommOperationDef;
import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;

public interface SpkiPrivateIdentityManager {
//    public byte[] sign(SignCommOperationContext context) throws Exception;
//    public byte[] decryptPrivate(DecryptOperationContext context) throws Exception;
    
    public SignOperationWorker buildSignWorker(SignCommOperationDef opDef) throws Exception;
    public DecryptOperationWorker buildPrivateDecryptWorker(EncryptCommOperationDef opDef) throws Exception;
    
    
}
