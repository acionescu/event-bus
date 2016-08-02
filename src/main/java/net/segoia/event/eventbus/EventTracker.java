package net.segoia.event.eventbus;

public class EventTracker {
    private boolean posted;
    
    
    
    public EventTracker(boolean posted) {
	super();
	this.posted = posted;
    }



    /**
     * @return the posted
     */
    public boolean isPosted() {
        return posted;
    }
}
