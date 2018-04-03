package net.segoia.event.eventbus;

import net.segoia.event.eventbus.peers.CustomEventListener;
import net.segoia.event.eventbus.peers.EventHandler;

public interface CustomEventContextListenerFactory {
    <E extends Event> CustomEventListener<E> build(EventHandler<E> handler);
}
