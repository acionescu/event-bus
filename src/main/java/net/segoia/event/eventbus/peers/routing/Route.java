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
