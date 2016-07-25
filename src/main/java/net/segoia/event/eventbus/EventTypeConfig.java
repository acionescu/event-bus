/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
