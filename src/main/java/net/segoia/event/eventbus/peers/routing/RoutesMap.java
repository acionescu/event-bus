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
