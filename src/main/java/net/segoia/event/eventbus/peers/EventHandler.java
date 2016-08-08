package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.CustomEventContext;
import net.segoia.event.eventbus.Event;

public interface EventHandler<E extends Event> {
    void handleEvent(CustomEventContext<E> event);
}
