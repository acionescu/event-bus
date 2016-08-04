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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This defines the header of an {@link Event} </br>
 * Contains a map of parameters and a set of tags
 * 
 * @author adi
 *
 */
public class EventHeader {
    private Map<String, Object> params;
    private Set<String> tags;

    /**
     * Hold the id of the event that caused this event to be triggered
     */
    private String causeEventId;

    /**
     * Holds the ids of the events that were triggered by this event
     */
    private Set<String> spawnedEventsIds = new LinkedHashSet<>();

    private String sourceNodeId;

    /**
     * Each time this event is forwarded to another bus, the relaying bus id is added here
     */
    private List<String> relayedBy = new ArrayList<>();

    /**
     * The event should only be relayed to the node with this address
     */
    private String to;

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
     * @param params
     *            the params to set
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
     * @param causeEventId
     *            the causeEventId to set
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
	relayedBy.add(busNodeId);
	if (sourceNodeId == null) {
	    sourceNodeId = busNodeId;
	}
    }

    /**
     * @return the sourceBusId
     */
    public String from() {
	return sourceNodeId;
    }

    public boolean wasRelayedBy(String busNodeId) {
	return relayedBy.contains(busNodeId);
    }

    public int relayHops() {
	return relayedBy.size();
    }

    public void to(String nodeId) {
	this.to = nodeId;
    }

    public String to() {
	return this.to;
    }

    /**
     * @return the relayedBy
     */
    public List<String> getRelayedBy() {
	return relayedBy;
    }

    public void replaceRelay(String oldId, String newId) {
	int i = relayedBy.indexOf(oldId);
	if (i >= 0) {
	    relayedBy.set(i, newId);
	    if (i == 0) {
		sourceNodeId = newId;
	    }
	}
    }

}
