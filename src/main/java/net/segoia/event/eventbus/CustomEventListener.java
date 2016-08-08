package net.segoia.event.eventbus;

public interface CustomEventListener<E extends Event> extends EventListener{

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.EventListener#onEvent(net.segoia.event.eventbus.EventContext)
     */
    void onEvent(CustomEventContext<E> ec);
    
    

}
