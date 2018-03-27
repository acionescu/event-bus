package net.segoia.event.eventbus.peers;

public abstract class GlobalEventNodeAgent extends EventNodeAgent {
    protected GlobalAgentEventNodeContext context;

    public void initGlobalContext(GlobalAgentEventNodeContext context) {
	this.context = context;
	init();
    }
}
