package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.CustomEventContext;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventListener;

public class CustomEventHandler<E extends Event> implements EventListener{
    private EventHandler<E> eventHandler;
    
    
    
    public CustomEventHandler(EventHandler<E> eventHandler) {
	super();
	this.eventHandler = eventHandler;
    }


    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.EventListener#onEvent(net.segoia.event.eventbus.EventContext)
     */
    @Override
    public void onEvent(EventContext ec) {
	eventHandler.handleEvent(new CustomEventContext<>(ec));
	
    }


    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.EventListener#init()
     */
    @Override
    public void init() {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.EventListener#terminate()
     */
    @Override
    public void terminate() {
	// TODO Auto-generated method stub
	
    }
    
}
