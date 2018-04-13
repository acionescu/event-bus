package net.segoia.event.eventbus.peers.security;

public interface CommManagerBuilder<C extends CommProtocolContext> {
    
    CommManager build(C context);
}
