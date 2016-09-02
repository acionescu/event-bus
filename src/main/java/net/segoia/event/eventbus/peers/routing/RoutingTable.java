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

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import net.segoia.util.data.SetMap;

/**
 * This keeps the possible routes from one node to others
 * 
 * @author adi
 *
 */
public class RoutingTable {
    /**
     * Keeps the possible routes to each known node
     */
    private Map<String, RoutesMap> forwardTable = new Hashtable<>();

    /**
     * Keeps the nodes that can be reached through each peer node
     */
    private SetMap<String, String> inverseTable = new SetMap<>();

    /**
     * Adds a new route
     * 
     * @param to
     *            - destination node
     * @param via
     *            - the next node in the route
     */
    public synchronized void addRoute(String to, String via) {
	if(routeExists(to, via)) {
	    return;
	}
	addForwardEntry(to, via);
	inverseTable.add(via, to);
    }

    public String getBestViaTo(String to) {
	RoutesMap rm = forwardTable.get(to);
	if (rm == null) {
	    return null;
	}
	return rm.getBestVia();
    }
    
    /**
     * Checks if we have at least one route to the target
     * @param to
     * @return
     */
    public boolean routeExists(String to) {
	RoutesMap routesMap = forwardTable.get(to);
	if(routesMap == null) {
	    return false;
	}
	return (routesMap.size() > 0);
    }
    
    public boolean routeExists(String to, String via) {
	RoutesMap routesMap = forwardTable.get(to);
	if(routesMap == null) {
	    return false;
	}
	return routesMap.hasVia(via);
    }

    protected void addForwardEntry(String to, String via) {
	RoutesMap rl = forwardTable.get(to);
	if (rl == null) {
	    rl = new RoutesMap();
	    forwardTable.put(to, rl);
	}
	rl.add(to, via);
    }

    /**
     * Removes the information for this node from the table, either if it's a target node or a via node
     * 
     * @param nodeId
     */
    public synchronized void removeAllFor(String nodeId) {
	/* see if this is a remote node */
	RoutesMap rl = forwardTable.remove(nodeId);
	if (rl == null) {
	    /* if not, then see if it's a via node */
	    Set<String> targets = inverseTable.remove(nodeId);
	    if (targets != null) {
		for (String to : targets) {
		    RoutesMap trl = forwardTable.get(to);
		    if (trl != null) {
			trl.remove(nodeId);
			if (trl.size() == 0) {
			    /* if this was the only route, cleanup */
			    forwardTable.remove(to);
			}
		    }
		}
	    }
	}
    }
}
