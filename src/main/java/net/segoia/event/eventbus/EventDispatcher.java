package net.segoia.event.eventbus;

/**
 * Dispatches an event to the registered listeners
 * 
 * @author adi
 *
 */
public interface EventDispatcher {

    boolean dispatchEvent(EventContext ec);

    void registerListener(EventListener listener);

    void registerListener(EventListener listener, int priority);

    void removeListener(EventListener listener);
}
