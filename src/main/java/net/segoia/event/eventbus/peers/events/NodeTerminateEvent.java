package net.segoia.event.eventbus.peers.events;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.peers.EventNode;

@EventType("EBUS:REQUEST:TERMINATE")
public class NodeTerminateEvent extends CustomEvent<EventNodeInfo>{

    public NodeTerminateEvent(EventNode node) {
	super(NodeTerminateEvent.class);
	this.data = new EventNodeInfo(node);
    }

}
