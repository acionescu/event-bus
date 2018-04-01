package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

public abstract class LocalEventNodeAgent extends EventNodeAgent {
    protected LocalAgentEventNodeContext context;

    public void initLocalContext(LocalAgentEventNodeContext context) {
	this.context = context;
	init();
    }

    protected abstract void registerHandlers(LocalAgentEventNodeContext context);

    @Override
    protected void registerHandlers() {
	registerHandlers(context);
    }

    protected void postEvent(Event event) {
	context.postEvent(event);
    }
    
}
