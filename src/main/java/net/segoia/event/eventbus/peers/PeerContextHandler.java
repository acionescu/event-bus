package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

public interface PeerContextHandler<E extends Event>{
    void handleEvent(PeerEventContext<E> context);
}
