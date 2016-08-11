package net.segoia.event.eventbus;


/**
 * This simply calls dispatch on the {@link EventContext}
 * @author adi
 *
 */
public class EventContextDispatcher implements EventDispatcher{

    @Override
    public void start() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void stop() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public boolean dispatchEvent(EventContext ec) {
	return ec.dispatch();
    }

    @Override
    public void registerListener(EventListener listener) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void registerListener(EventListener listener, int priority) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void removeListener(EventListener listener) {
	// TODO Auto-generated method stub
	
    }

}
