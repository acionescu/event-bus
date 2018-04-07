package net.segoia.event.eventbus.peers.security;

public interface SpkiPrivateIdentityManager {
    public byte[] sign(SignCommOperationContext context) throws Exception;
    public byte[] decryptPrivate(DecryptOperationContext context) throws Exception;
}
