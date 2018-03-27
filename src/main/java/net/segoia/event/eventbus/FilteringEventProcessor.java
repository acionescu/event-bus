package net.segoia.event.eventbus;

import java.util.HashMap;
import java.util.Map;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.EventClassMatchCondition;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.eventbus.peers.CustomEventListener;
import net.segoia.event.eventbus.peers.EventHandler;

public class FilteringEventProcessor extends SimpleEventProcessor {
    private Map<Condition, FilteringEventDispatcher> conditionedListeners = new HashMap<>();

    public FilteringEventProcessor() {
	super();
    }

    public FilteringEventProcessor(EventDispatcher eventDispatcher) {
	super(eventDispatcher);
    }

    protected FilteringEventDispatcher getListenerForCondition(Condition condition, int priority) {
	FilteringEventDispatcher l = conditionedListeners.get(condition);
	if (l == null) {
	    l = createEventDispatcherForCondition(condition);
	    conditionedListeners.put(condition, l);
	    if (priority >= 0) {
		registerListener(l, priority);
	    } else {
		registerListener(l);
	    }
	}
	return l;
    }

    protected FilteringEventDispatcher createEventDispatcherForCondition(Condition condition) {
	return new FilteringEventDispatcher(condition, new SimpleEventDispatcher());
    }

    /**
     * Registers a listener for a particular condition with no special priority
     * 
     * @param condition
     * @param listener
     */
    public void registerListener(Condition condition, EventContextListener listener) {
	getListenerForCondition(condition, -1).registerListener(listener);
    }

    /**
     * Registers a listener for a particular condition with a given priority
     * 
     * @param condition
     * @param listener
     * @param priority
     *            - this is the priority of this listener among other listeners registered for the same condition
     */
    public void registerListener(Condition condition, EventContextListener listener, int priority) {
	getListenerForCondition(condition, -1).registerListener(listener, priority);
    }

    /**
     * Registers a listener for a particular condition with a given priority for the condition listener and the final
     * listener
     * 
     * @param condition
     * @param cPriority
     *            - the priority of the condition filter among other event bus top level listeners
     * @param listener
     * @param lPriority
     *            - final listener priority, among other listeners registered for this condition
     */
    public void registerListener(Condition condition, int cPriority, EventContextListener listener, int lPriority) {
	getListenerForCondition(condition, cPriority).registerListener(listener, lPriority);
    }

    /**
     * Registers a listener for a particular condition with a given priority for the condition listener
     * 
     * @param condition
     * @param cPriority
     * @param listener
     */
    public void registerListener(Condition condition, int cPriority, EventContextListener listener) {
	getListenerForCondition(condition, cPriority).registerListener(listener);
    }

    public <E extends Event> void addEventHandler(EventHandler<E> handler) {
	registerListener(getCustomEventListener(handler));
    }

    public <E extends Event> void addEventHandler(EventHandler<E> handler, int priority) {
	registerListener(getCustomEventListener(handler), priority);
    }

    public <E extends Event> void addEventHandler(Condition condition, EventHandler<E> handler) {
	registerListener(condition, getCustomEventListener(handler));
    }

    public <E extends Event> void addEventHandler(Class<E> eventClass, EventHandler<E> handler) {
	addEventHandler(new EventClassMatchCondition(eventClass), handler);
    }

    public <E extends Event> void addEventHandler(String eventType, EventHandler<E> handler) {
	addEventHandler(new StrictEventMatchCondition(eventType), handler);
    }

    protected <E extends Event> CustomEventListener<E> getCustomEventListener(EventHandler<E> handler) {
	return new CustomEventListener<>(handler);
    }
}
