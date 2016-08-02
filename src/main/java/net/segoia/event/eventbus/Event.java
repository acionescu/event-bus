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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.segoia.event.eventbus.util.JsonUtils;

public class Event extends AbstractEvent {
    public static final String etSep = ":";

    private EventHeader header = new EventHeader();


    /**
     * The scope of the event, providing a way to set a certain concern area ( e.g. SYSTEM, APP, ... )
     */
    private String scope;

    /**
     * A way to categorize messages by their meaning ( e.g. message, alert, error, notification, ... )
     */
    private String category;

    /**
     * A particular name for this event, usually some type of action
     */
    private String name;

    /**
     * A key that uniquely identifies the component that generated the event within the set scope
     */
    private String topic;

    
    /**
     * Event's creation timestamp
     */
    private long ts;

    /**
     * Once set to true this event will not permit any modification to its structure, except the header
     */
    private transient boolean closed;

    private Map<String, Object> params = new HashMap<>();

    public Event(String et) {
	if (et == null) {
	    throw new IllegalArgumentException("Event type cannot be null");
	}

	String[] etArray = et.split(etSep);
	if (etArray.length != 3) {
	    throw new IllegalArgumentException("Event type should have the format <scope>:<category>:<name>");
	}

	init(etArray[0], etArray[1], etArray[2]);
    }

    public Event(String et, String topic) {
	this(et);
	this.topic = topic;
    }

    /**
     * Initializes this event
     * 
     * @param scope
     * @param category
     * @param name
     */
    private void init(String scope, String category, String name) {
	if (scope == null) {
	    throw new IllegalArgumentException("Scope cannot be null");
	}
	if (category == null) {
	    throw new IllegalArgumentException("Cagegory cannot be null");
	}
	if (name == null) {
	    throw new IllegalArgumentException("Name cannot be null");
	}

	this.scope = scope;
	this.category = category;
	this.name = name;

	this.ts = System.currentTimeMillis();
    }

    public Event(String scope, String category, String name) {

	init(scope, category, name);
    }

    public Event(String scope, String category, String name, String topic) {

	this(scope, category, name);
	this.topic = topic;
    }

    public Event(String scope, String category, String name, Event cause) {

	init(scope, category, name);
	if (cause != null) {
	    cause.setAsCauseFor(this);
	}
    }

    public Event(String scope, String category, String name, Event cause, String topic) {

	this(scope, category, name, cause);
	this.topic = topic;
    }
    
    
    public static Event fromJson(String json) {
		
	Event e = JsonUtils.fromJson(json, Event.class);
	if(e.header == null) {
	    e.header=new EventHeader();
	}
	e.close();
	return e;
    }
    
    public String toJson() {
	return JsonUtils.toJson(this);
    }

    protected void lazyInit() {
	this.id = UUID.randomUUID().toString();
	if (et == null) {
	    /**
	     * event type - we can formalize this as: <scope>:<category>:<name> ( e.g. system:error:db-connection-failed )
	     */
	    this.et = new StringBuffer().append(scope).append(etSep).append(category).append(etSep).append(name)
		    .toString();
	}
    }

    /**
     * Generates a new event from this event, setting this one as the cause of the generated one
     * 
     * @param et
     * @return
     */
    protected Event setAsCauseFor(Event newEvent) {
	newEvent.header.setCauseEventId(this.getId());
	this.header.addSpawnedEventId(newEvent.getId());
	return newEvent;
    }

    public Event setHeaderParam(String key, Object value) {
	header.addParam(key, value);
	return this;
    }

    public Object getHeaderParam(String key, Object defaultValue) {
	Object hp = header.getParam(key);
	if (hp == null) {
	    return defaultValue;
	}
	return hp;
    }

    public Object getHeaderParam(String key) {
	return header.getParam(key);
    }
    
    public void addHeaderParam(String key, Object value) {
	header.addParam(key, value);
    }

    public Event tag(String tag) {
	header.addTag(tag);
	return this;
    }

    public boolean hasTag(String tag) {
	return header.hasTag(tag);
    }

    public Object getParam(String key) {
	return params.get(key);
    }

    public Event addParam(String key, Object value) {
	if (isClosed()) {
	    throw new UnsupportedOperationException("This event is closed");
	}
	params.put(key, value);
	return this;
    }

    public Event addParams(Map<String, Object> params) {
	this.params.putAll(params);
	return this;
    }

    /**
     * @return the ts
     */
    public long getTs() {
	return ts;
    }

    /**
     * Will close this event for modification, except for header data
     */
    public void close() {
	closed = true;
    }

    /**
     * @return the closed
     */
    public boolean isClosed() {
	return closed;
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

    public String causeEventId() {
	return header.getCauseEventId();
    }

    public Set<String> getSpawnedEventsIds() {
	return header.getSpawnedEventsIds();
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }
    
    

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic;
    }
    
    
    
    public void addRelay(String busNodeId) {
	header.addRelay(busNodeId);
    }

    /**
     * @return the sourceBusId
     */
    public String sourceBusId() {
       return header.getSourceBusId();
    }
    
    public boolean wasRelayedBy(String busNodeId) {
	return header.wasRelayedBy(busNodeId);
    }
    
    
    public int relayHops() {
	return header.relayHops();
    }
    
    
    public void to(String nodeId) {
	header.to(nodeId);
    }
    
    public String to() {
	return header.to();
    }
    

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((params == null) ? 0 : params.hashCode());
	result = prime * result + ((topic == null) ? 0 : topic.hashCode());
	result = prime * result + (int) (ts ^ (ts >>> 32));
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Event other = (Event) obj;
	if (params == null) {
	    if (other.params != null)
		return false;
	} else if (!params.equals(other.params))
	    return false;
	if (topic == null) {
	    if (other.topic != null)
		return false;
	} else if (!topic.equals(other.topic))
	    return false;
	if (ts != other.ts)
	    return false;
	return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Event [");
	if (super.toString() != null)
	    builder.append("toString()=").append(super.toString()).append(", ");
	if (id != null)
	    builder.append("id=").append(id).append(", ");
	if (et != null)
	    builder.append("et=").append(et).append(", ");
	if (causeEventId() != null)
	    builder.append("causeEventId=").append(causeEventId()).append(", ");
	if (sourceBusId() != null)
	    builder.append("sourceBusId=").append(sourceBusId()).append(", ");
	if(to() != null)
	    builder.append("to=").append(to()).append(", ");
	builder.append("relayHops=").append(relayHops()).append(", ");
	if (topic != null)
	    builder.append("topic=").append(topic).append(", ");
	builder.append("ts=").append(ts).append(", ");
	if (params != null)
	    builder.append("params=").append(params);
	builder.append("]");
	return builder.toString();
    }

//    /* (non-Javadoc)
//     * @see java.lang.Object#toString()
//     */
//    @Override
//    public String toString() {
//	StringBuilder builder = new StringBuilder();
//	builder.append("Event [");
//	if (super.toString() != null)
//	    builder.append("toString()=").append(super.toString()).append(", ");
//	if (id != null)
//	    builder.append("id=").append(id).append(", ");
//	if (et != null)
//	    builder.append("et=").append(et).append(", ");
//	if (causeEventId() != null)
//	    builder.append("causeEventId=").append(causeEventId()).append(", ");
//	if (topic != null)
//	    builder.append("topic=").append(topic).append(", ");
//	builder.append("ts=").append(ts).append(", ");
//	if (params != null)
//	    builder.append("params=").append(params);
//	builder.append("]");
//	return builder.toString();
//    }

    
}
