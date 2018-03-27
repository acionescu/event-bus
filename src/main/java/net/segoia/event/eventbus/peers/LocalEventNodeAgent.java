package net.segoia.event.eventbus.peers;

public abstract class LocalEventNodeAgent extends EventNodeAgent{
    protected LocalAgentEventNodeContext context;
    
    public void initLocalContext(LocalAgentEventNodeContext context) {
	this.context = context;
	init();
    }
}
