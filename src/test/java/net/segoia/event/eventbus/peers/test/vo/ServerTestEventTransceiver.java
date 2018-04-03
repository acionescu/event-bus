package net.segoia.event.eventbus.peers.test.vo;

import net.segoia.event.eventbus.peers.EventNode;
import net.segoia.event.eventbus.peers.events.bind.PeerBindRequest;

public class ServerTestEventTransceiver extends TestEventTransceiver{
    /**
     * The event node that will handle this connection
     */
    private EventNode node;
    
    
    public ServerTestEventTransceiver(EventNode node) {
	this.node = node;
    }

    @Override
    public void start() {
	node.registerPeer(new PeerBindRequest(this));
    }

    @Override
    public void init() {
	// TODO Auto-generated method stub
	
    }

}
