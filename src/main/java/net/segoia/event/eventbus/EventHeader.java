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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    
    
    private String sourceBusId;
    
    /**
     * Each time this event is forwarded to another bus, the relaying bus id is added here
     */
    private Deque<String> relayedBy = new ArrayDeque<>();
    
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
    
    public void addRelay(String busNodeId) {
	relayedBy.push(busNodeId);
	if(sourceBusId == null) {
	    sourceBusId = busNodeId;
	}
    }

    /**
     * @return the sourceBusId
     */
    public String getSourceBusId() {
        return sourceBusId;
    }
    
    public boolean wasRelayedBy(String busNodeId) {
	return relayedBy.contains(busNodeId);
    }
    
    public String lastRelay() {
	return relayedBy.peek();
    }
    
    public int relayHops() {
	return relayedBy.size();
    }
    
    
}
