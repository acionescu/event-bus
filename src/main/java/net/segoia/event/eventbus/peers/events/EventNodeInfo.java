package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.peers.EventNode;

public class EventNodeInfo {
    private EventNode node;

    public EventNodeInfo(EventNode node) {
	super();
	this.node = node;
    }

    /**
     * @return the node
     */
    public EventNode getNode() {
        return node;
    }
    
    
}
