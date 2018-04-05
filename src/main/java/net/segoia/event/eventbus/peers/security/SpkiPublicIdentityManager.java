package net.segoia.event.eventbus.peers.security;

public interface SpkiPublicIdentityManager extends PublicIdentityManager{
    public byte[] ecryptPublic(EncryptOperationContext context);
    public boolean verifySignature(VerifySignatureOperationContext context);
}
