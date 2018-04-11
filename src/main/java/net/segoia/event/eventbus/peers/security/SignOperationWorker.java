package net.segoia.event.eventbus.peers.security;

public interface SignOperationWorker {
    byte[] sign(byte[] data) throws Exception;
}
