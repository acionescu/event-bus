package net.segoia.event.eventbus.config.json;

import java.util.List;

import com.google.gson.JsonObject;

import net.segoia.event.eventbus.EventListener;

public class EventListenerJsonConfig{
    
    private EventListener instance;
    private List<String> conditions;
    private int priority;
    
    
    /**
     * @return the conditions
     */
    public List<String> getConditions() {
        return conditions;
    }
    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * @param conditions the conditions to set
     */
    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }
    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
    /**
     * @return the instance
     */
    public EventListener getInstance() {
        return instance;
    }
    /**
     * @param instance the instance to set
     */
    public void setInstance(EventListener instance) {
        this.instance = instance;
    }
    
    
}
