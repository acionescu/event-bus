package net.segoia.event.eventbus;

public class EventRights {
    /**
     * Controls if an event's posting to the bus is allowed
     */
    private boolean allowed=true;

    
    
    
    public EventRights() {
	
    }

    public EventRights(boolean allowed) {
	super();
	this.allowed = allowed;
    }

    /**
     * @return the allowed
     */
    public boolean isAllowed() {
        return allowed;
    }
    
    
    
}
