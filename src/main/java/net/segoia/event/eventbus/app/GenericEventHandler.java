package net.segoia.event.eventbus.app;

import net.segoia.event.eventbus.CustomEventContext;
import net.segoia.event.eventbus.Event;

public interface GenericEventHandler<E extends Event, C extends CustomEventContext<E>> {

    void handleEvent(C context);

}
