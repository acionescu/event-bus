package net.segoia.event.eventbus.peers;

import java.util.Set;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.vo.bind.ConnectToPeerRequest;

/**
 * A context that is aware of peer events as well as local events
 * @author adi
 *
 */
public class GlobalAgentEventNodeContext extends LocalAgentEventNodeContext{
    private PeersManager peersManager;

    public GlobalAgentEventNodeContext(EventNodeContext nodeContext, PeersManager peersManager) {
	super(nodeContext);
	this.peersManager = peersManager;
    }

    public void forwardTo(Event event, String peerId) {
	peersManager.forwardTo(event, peerId);
    }
    
    public void forwardTo(Event event, Set<String> peerIds) {
	peersManager.forwardTo(event, peerIds);
    }
    
    public void forwardToDirectPeers(Event event) {
	peersManager.forwardToDirectPeers(event);
    }
    
    public void forwardToAllKnown(Event event) {
	peersManager.forwardToAllKnown(event);
    }
    
   
}
