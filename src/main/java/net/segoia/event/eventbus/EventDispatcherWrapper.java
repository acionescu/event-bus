package net.segoia.event.eventbus;

public abstract class EventDispatcherWrapper implements EventDispatcher{
    protected EventDispatcher nestedDispatcher;

    public EventDispatcherWrapper(EventDispatcher nestedDispatcher) {
	super();
	this.nestedDispatcher = nestedDispatcher;
    }

    @Override
    public void registerListener(EventListener listener) {
	nestedDispatcher.registerListener(listener);
    }

    @Override
    public void registerListener(EventListener listener, int priority) {
	nestedDispatcher.registerListener(listener, priority);
    }

    @Override
    public void removeListener(EventListener listener) {
	nestedDispatcher.registerListener(listener);

    }
    
    @Override
    public void start() {
	nestedDispatcher.start();
	
    }

    @Override
    public void stop() {
	nestedDispatcher.stop();
	
    }

}
