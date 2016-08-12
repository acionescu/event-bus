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
package net.segoia.event.eventbus.peers.routing;

/**
 * This keeps the information needed to reach another node through a unique path
 * 
 * @author adi
 *
 */
public class Route implements Comparable<Route> {
    /**
     * The next node from the path to destination
     */
    private String nextNodeId;

    /**
     * The node we want to reach
     */
    private String targetNodeId;
    /**
     * This is a simple float reflecting how usable this route is </br>
     * This may be computed by a more complex algorithm (e.g. considering transmission efficiency, velocity, etc.. )
     */
    private float usability;

    public Route(String nextNodeId, String targetNodeId) {
	super();
	this.nextNodeId = nextNodeId;
	this.targetNodeId = targetNodeId;
    }

    /**
     * @return the nextNodeId
     */
    public String getNextNodeId() {
	return nextNodeId;
    }

    /**
     * @return the targetNodeId
     */
    public String getTargetNodeId() {
	return targetNodeId;
    }

    /**
     * @return the usability
     */
    public float getUsability() {
	return usability;
    }

    @Override
    public int compareTo(Route o) {
	if(this == o) {
	    return 0;
	}
	if (!targetNodeId.equals(o.targetNodeId)) {
	    throw new IllegalArgumentException("Target node " + targetNodeId + " not matching " + o.targetNodeId);
	}

	if (usability > o.usability) {
	    return -1;
	} else if (usability < o.usability) {
	    return 1;
	}

	return 0;
    }
    
    

}
