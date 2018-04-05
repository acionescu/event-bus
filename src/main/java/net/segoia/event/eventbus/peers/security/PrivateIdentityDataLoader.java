package net.segoia.event.eventbus.peers.security;

public interface PrivateIdentityDataLoader<D> {
    public void load();
    public D getData();
}
