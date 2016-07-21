package net.segoia.event.eventbus;

public class EventTracker {
    private EventContext eventContext;
    private boolean posted;
    
    
    public EventTracker(EventContext eventContext, boolean posted) {
	super();
	this.eventContext = eventContext;
	this.posted = posted;
    }


    /**
     * @return the posted
     */
    public boolean isPosted() {
        return posted;
    }
    
    
    
}
