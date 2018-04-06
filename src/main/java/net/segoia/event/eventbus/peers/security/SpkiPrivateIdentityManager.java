package net.segoia.event.eventbus.peers.security;

public interface SpkiPrivateIdentityManager {
    public byte[] sign(SignCommOperationContext context);
    public byte[] decryptPrivate(DecryptOperationContext context);
}
