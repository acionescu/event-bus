package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.Event;

public class EventBuilderContext {
    /** 
     * incremented with 1 on every new copy
     */
    private int depth=0;
    private String scope;
    private String category;
    private String name;
    private String topic;
    private Event causeEvent;
    
    
    
    public EventBuilderContext copy() {
	EventBuilderContext c = new EventBuilderContext();
	c.scope = scope;
	c.category = category;
	c.name = name;
	c.topic = topic;
	c.causeEvent = causeEvent;
	c.depth = depth+1;
	
	return c;
    }
    
    /**
     * @return the scope
     */
    public String getScope() {
        return scope;
    }
    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
   
    /**
     * @param scope the scope to set
     */
    public void setScope(String scope) {
        this.scope = scope;
    }
    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
   
    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }
    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * @return the causeEvent
     */
    public Event getCauseEvent() {
        return causeEvent;
    }

    /**
     * @param causeEvent the causeEvent to set
     */
    public void setCauseEvent(Event causeEvent) {
        this.causeEvent = causeEvent;
    }

    /**
     * @return the depth
     */
    protected int getDepth() {
        return depth;
    }
    
        
}
