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
