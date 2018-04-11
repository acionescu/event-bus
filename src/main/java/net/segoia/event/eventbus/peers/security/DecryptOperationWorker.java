package net.segoia.event.eventbus.peers.security;

public interface DecryptOperationWorker {
    byte[] decrypt(byte[] data) throws Exception;
}
