package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;
import net.segoia.event.eventbus.Event;

/**
 * A context for the event node agents that are only aware of the local context
 * 
 * @author adi
 *
 */
public class LocalAgentEventNodeContext {
    private EventNodeContext nodeContext;

    public LocalAgentEventNodeContext(EventNodeContext nodeContext) {
	super();
	this.nodeContext = nodeContext;
    }

    public <E extends Event> void addEventHandler(Class<E> eventClass, EventHandler<E> handler) {
	nodeContext.getNode().addEventHandler(eventClass, handler);
    }
    
    public <E extends Event> void addEventHandler(String eventType, EventHandler<E> handler) {
	nodeContext.getNode().addEventHandler(eventType, handler);
    }
    
    public <E extends Event> void addEventHandler(EventHandler<E> handler) {
	nodeContext.getNode().addEventHandler(handler);
    }
    
    public <E extends Event> void addEventHandler(Condition cond, EventHandler<E> handler) {
	nodeContext.getNode().addEventHandler(cond,handler);
    }

    public void postEvent(Event event) {
	nodeContext.postEvent(event);
    }

}
