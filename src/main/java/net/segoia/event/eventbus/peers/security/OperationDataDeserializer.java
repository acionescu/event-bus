package net.segoia.event.eventbus.peers.security;

public interface OperationDataDeserializer<T> {
    public T toObject(byte[] data);
}
