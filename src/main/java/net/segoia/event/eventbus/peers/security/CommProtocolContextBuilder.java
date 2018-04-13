package net.segoia.event.eventbus.peers.security;

public interface CommProtocolContextBuilder<C extends CommProtocolContext> {
    C build(PeerCommContext context);
}
