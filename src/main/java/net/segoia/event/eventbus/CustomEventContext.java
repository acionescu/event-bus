package net.segoia.event.eventbus;

public class CustomEventContext<E extends Event> extends EventContext{
    
    public CustomEventContext(EventContext ec) {
	super(ec.getEvent(),ec.bus());
    }

    public CustomEventContext(Event event, EventBus eventBus) {
	super(event, eventBus);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.EventContext#event()
     */
    @Override
    public E event() {
	return (E)super.event();
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.EventContext#getEvent()
     */
    @Override
    public E getEvent() {
	return (E)super.getEvent();
    }
    
    

}
