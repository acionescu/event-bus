package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

public class RemoteEventContext<E extends Event, N extends EventNode> {
    private N currentNode;
    private PeerEventContext peerContext;

    public RemoteEventContext(N currentNode, PeerEventContext peerContext) {
	super();
	this.currentNode = currentNode;
	this.peerContext = peerContext;
    }

    /**
     * @return the currentNode
     */
    public N getCurrentNode() {
	return currentNode;
    }

    /**
     * @return the peerContext
     */
    public PeerEventContext getPeerContext() {
	return peerContext;
    }

    public E getEvent() {
	return (E) peerContext.getEvent();
    }
}
