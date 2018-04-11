package net.segoia.event.eventbus.peers.security;

public interface SpkiPublicIdentityManager extends PublicIdentityManager{
    public byte[] ecryptPublic(EncryptOperationContext context) throws Exception;
    public boolean verifySignature(VerifySignatureOperationContext context) throws Exception;
}
