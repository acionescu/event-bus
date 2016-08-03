package net.segoia.event.eventbus.peers;

import java.util.HashMap;
import java.util.Map;


/**
 * An agent is a node that reacts to events in a certain way
 * </br>
 * It can also post events as a public node, or hiding behind a relay node
 * @author adi
 *
 */
public abstract class AgentNode extends EventNode {
    protected EventNode mainNode;

    private Map<String, RemoteEventHandler<?>> handlers = new HashMap<>();

    private boolean hasHandlers = false;

    @Override
    protected EventRelay buildLocalRelay(String peerId) {
	return new DefaultEventRelay(peerId, this);
    }

    protected void addEventHandler(String eventType, RemoteEventHandler<?> handler) {
	handlers.put(eventType, handler);
	hasHandlers = true;
    }

    protected void removeEventHandlers(String eventType) {
	handlers.remove(eventType);
	hasHandlers = (handlers.size() > 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.segoia.event.eventbus.peers.EventBusNode#handleRemoteEvent(net.segoia.event.eventbus.peers.PeerEventContext)
     */
    @Override
    protected void handleRemoteEvent(PeerEventContext pc) {
	if (!hasHandlers) {
	    handleEvent(pc.getEvent());
	    return;
	}
	String et = pc.getEvent().getEt();
	RemoteEventHandler h = handlers.get(et);
	if (h != null) {
	    h.handleRemoteEvent(new RemoteEventContext<AgentNode>(this, pc));
	}

    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#onPeerLeaving(java.lang.String)
     */
    @Override
    public void onPeerLeaving(String peerId) {
	super.onPeerLeaving(peerId);
	/* since an agent is bound by the main node, if that leaves we need to finish too */
	terminate();
    }

}
