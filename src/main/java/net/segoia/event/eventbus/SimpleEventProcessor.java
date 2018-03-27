package net.segoia.event.eventbus;

public class SimpleEventProcessor {
    private EventDispatcher eventDispatcher = new SimpleEventDispatcher();

    public SimpleEventProcessor(EventDispatcher eventDispatcher) {
	super();
	this.eventDispatcher = eventDispatcher;
    }

    public SimpleEventProcessor() {
	super();
    }

    public void start() {
	eventDispatcher.start();
    }

    public void stop() {
	eventDispatcher.stop();
    }

    public boolean processEvent(EventContext ec) {
	return eventDispatcher.dispatchEvent(ec);
    }

    public void registerListener(EventContextListener listener) {
	eventDispatcher.registerListener(listener);
    }

    public void removeListener(EventContextListener listener) {
	eventDispatcher.removeListener(listener);
    }

    public void registerListener(EventContextListener listener, int priority) {
	eventDispatcher.registerListener(listener, priority);

    }
    
    /**
     * @return the eventDispatcher
     */
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * @param eventDispatcher the eventDispatcher to set
     */
    public void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

}
