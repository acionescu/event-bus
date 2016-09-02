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

import java.util.TreeMap;

/**
 * This keeps the possible routes from one node to another
 * @author adi
 *
 */
public class RoutesMap {
    /**
     * Keeps routes ordered by usability ( best one first )
     */
    private TreeMap<String, Route> routes=new TreeMap<>();
    
    
    public Route getBest() {
	return routes.firstEntry().getValue();
    }
    
    public String getBestVia() {
	Route best = getBest();
	if(best == null) {
	    return null;
	}
	return best.getNextNodeId();
    }
    
    public boolean hasVia(String via) {
	return routes.containsKey(via);
    }
    
    public void add(String to, String via) {
	Route r = new Route(via, to);
	routes.put(via,r);
    }
    
    public void remove(String via) {
	routes.remove(via);
    }
    
    public int size() {
	return routes.size();
    }
}
