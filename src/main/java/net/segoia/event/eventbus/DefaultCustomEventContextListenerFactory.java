package net.segoia.event.eventbus;

import net.segoia.event.eventbus.peers.CustomEventListener;
import net.segoia.event.eventbus.peers.EventHandler;

public class DefaultCustomEventContextListenerFactory implements CustomEventContextListenerFactory{

    @Override
    public <E extends Event> CustomEventListener<E> build(EventHandler<E> handler) {
	return new CustomEventListener<>(handler);
    }

}