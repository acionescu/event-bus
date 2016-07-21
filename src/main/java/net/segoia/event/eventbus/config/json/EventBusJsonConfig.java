package net.segoia.event.eventbus.config.json;

import java.util.Map;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.EventBus;
import net.segoia.event.eventbus.EventBusConfig;

public class EventBusJsonConfig extends EventBusConfig{
    
    /**
     * What implementation of {@link EventBus} to use
     */
    private String busClassName;
    private Map<String,Condition> conditions;
    private Map<String, EventListenerJsonConfig> listeners;
        
    
    /**
     * @return the busClassName
     */
    public String getBusClassName() {
        return busClassName;
    }
    /**
     * @param busClassName the busClassName to set
     */
    public void setBusClassName(String busClassName) {
        this.busClassName = busClassName;
    }
    /**
     * @return the conditions
     */
    public Map<String, Condition> getConditions() {
        return conditions;
    }
    /**
     * @return the listeners
     */
    public Map<String, EventListenerJsonConfig> getListeners() {
        return listeners;
    }
    /**
     * @param conditions the conditions to set
     */
    public void setConditions(Map<String, Condition> conditions) {
        this.conditions = conditions;
    }
    /**
     * @param listeners the listeners to set
     */
    public void setListeners(Map<String, EventListenerJsonConfig> listeners) {
        this.listeners = listeners;
    }
        
}
