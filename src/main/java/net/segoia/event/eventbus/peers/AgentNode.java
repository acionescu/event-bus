package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.TrueCondition;

/**
 * An agent is a node that reacts to events in a certain way </br>
 * It can also post events as a public node, or hiding behind a relay node
 * 
 * @author adi
 *
 */
public abstract class AgentNode extends EventNode {
    protected EventNode mainNode;
  
    
    public AgentNode() {
	this(true);
    }

    public AgentNode(boolean autoinit) {
	super(autoinit);
    }

    public AgentNode(EventBusNodeConfig config) {
	super(config);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#nodeInit()
     */
    @Override
    protected void nodeInit() {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.peers.EventNode#nodeConfig()
     */
    @Override
    protected void nodeConfig() {
	// TODO Auto-generated method stub
	
    }

    protected void setRequestedEventsCondition() {
	// TODO: implement this, for now request all
	config.setDefaultRequestedEvents(new TrueCondition());
    }

    @Override
    protected EventRelay buildLocalRelay(String peerId) {
	return new DefaultEventRelay(peerId, this);
    }


    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.event.eventbus.peers.EventNode#onPeerLeaving(java.lang.String)
     */
    @Override
    public void onPeerLeaving(String peerId) {
	super.onPeerLeaving(peerId);
	/* since an agent is bound by the main node, if that leaves we need to finish too */
	terminate();
    }

}
