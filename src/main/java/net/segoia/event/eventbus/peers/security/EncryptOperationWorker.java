package net.segoia.event.eventbus.peers.security;

public interface EncryptOperationWorker {
     byte[] encrypt(byte[] data) throws Exception;
}
