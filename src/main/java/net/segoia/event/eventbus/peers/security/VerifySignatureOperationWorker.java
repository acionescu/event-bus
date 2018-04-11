package net.segoia.event.eventbus.peers.security;

public interface VerifySignatureOperationWorker {
    boolean verify(byte[] data, byte[] signature) throws Exception;
}
