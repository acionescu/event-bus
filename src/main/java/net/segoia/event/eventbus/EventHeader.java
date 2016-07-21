package net.segoia.event.eventbus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * This defines the header of an {@link Event}
 * </br>
 * Contains a map of parameters and a set of tags
 * @author adi
 *
 */
public class EventHeader {
    private Map<String,Object> params;
    private Set<String> tags;
    
    /**
     * Hold the id of the event that caused this event to be triggered
     */
    private String causeEventId;
    
    /**
     * Holds the ids of the events that were triggered by this event
     */
    private Set<String> spawnedEventsIds = new HashSet<>();
    
    public EventHeader() {
	params = new HashMap<>();
	tags = new HashSet<>();
    }

    public EventHeader addParam(String key, Object value) {
	params.put(key, value);
	return this;
    }
    
    public EventHeader addTag(String tag) {
	tags.add(tag);
	return this;
    }
    
    public Object getParam(String key) {
	return params.get(key);
    }
    
    public boolean hasTag(String tag) {
	return tags.contains(tag);
    }
    

    /**
     * @return the params
     */
    public Map<String, Object> getParams() {
        return params;
    }


    /**
     * @return the tags
     */
    public Set<String> getTags() {
        return tags;
    }


    /**
     * @param params the params to set
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

   
    /**
     * @return the causeEventId
     */
    public String getCauseEventId() {
        return causeEventId;
    }

    /**
     * @param causeEventId the causeEventId to set
     */
    public void setCauseEventId(String causeEventId) {
        this.causeEventId = causeEventId;
    }
    
    public void addSpawnedEventId(String id) {
	spawnedEventsIds.add(id);
    }

    /**
     * @return the spawnedEventsIds
     */
    public Set<String> getSpawnedEventsIds() {
        return spawnedEventsIds;
    }
    
}
