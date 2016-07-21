package net.segoia.event.eventbus;

import net.segoia.util.logging.LoggingLevel;


/**
 * A configuration for a particular event type
 * </br>
 * 
 * This defines a generic way to tweak the handling of an event of a certain type
 * 
 * </br>
 * 
 * Only properties of global concern should be defined here
 * @author adi
 *
 */
public class EventTypeConfig {
    
    /**
     * If set to false the event will not be logged
     */
    private boolean loggingOn;
    /**
     * logging level
     */
    private LoggingLevel loggingLevel=LoggingLevel.INFO;
    
    
    private EventRights eventRights = new EventRights();
    
    /**
     * @return the loggingOn
     */
    public boolean isLoggingOn() {
        return loggingOn;
    }
    
    /**
     * @param loggingOn the loggingOn to set
     */
    public void setLoggingOn(boolean loggingOn) {
        this.loggingOn = loggingOn;
    }

    /**
     * @return the loggingLevel
     */
    public LoggingLevel getLoggingLevel() {
        return loggingLevel;
    }

    /**
     * @param loggingLevel the loggingLevel to set
     */
    public void setLoggingLevel(LoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    /**
     * @return the eventRights
     */
    public EventRights getEventRights() {
        return eventRights;
    }

    /**
     * @param eventRights the eventRights to set
     */
    public void setEventRights(EventRights eventRights) {
        this.eventRights = eventRights;
    }
    
    
}
