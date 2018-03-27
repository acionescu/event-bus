package net.segoia.event.eventbus.peers;

public abstract class EventNodeAgent {
    protected void init() {
	config();
	registerHandlers();
    }
    public abstract void terminate();
    protected abstract void config();
    protected abstract void registerHandlers();
}
