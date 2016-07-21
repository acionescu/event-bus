package net.segoia.event.eventbus;

import java.util.HashMap;
import java.util.Map;

import net.segoia.event.conditions.Condition;

public class FilteringEventBus extends SimpleEventBus {
    private Map<Condition, FilteringEventListener> conditionedListeners = new HashMap<>();

    /**
     * Registers a listener for a particular condition with no special priority
     * 
     * @param condition
     * @param listener
     */
    public void registerListener(Condition condition, EventListener listener) {
	getListenerForCondition(condition,-1).registerListener(listener);
    }

    /**
     * Registers a listener for a particular condition with a given priority
     * 
     * @param condition
     * @param listener
     * @param priority
     *            - this is the priority of this listener among other listeners registered for the same condition
     */
    public void registerListener(Condition condition, EventListener listener, int priority) {
	getListenerForCondition(condition,-1).registerListener(listener, priority);
    }

    /**
     * Registers a listener for a particular condition with a given priority for the condition listener and the final listener 
     * @param condition
     * @param cPriority - condition listener priority
     * @param listener
     * @param lPriority - final listener priority
     */
    public void registerListener(Condition condition, int cPriority, EventListener listener, int lPriority) {
	getListenerForCondition(condition, cPriority).registerListener(listener, lPriority);
    }
    
    /**
     * Registers a listener for a particular condition with a given priority for the condition listener
     * @param condition
     * @param cPriority
     * @param listener
     */
    public void registerListener(Condition condition, int cPriority, EventListener listener) {
	getListenerForCondition(condition, cPriority).registerListener(listener);
    }

    private FilteringEventListener getListenerForCondition(Condition condition, int priority) {
	FilteringEventListener l = conditionedListeners.get(condition);
	if (l == null) {
	    l = new FilteringEventListener(condition);
	    conditionedListeners.put(condition, l);
	    if (priority >= 0) {
		registerListener(l, priority);
	    } else {
		registerListener(l);
	    }
	}
	return l;
    }

}
