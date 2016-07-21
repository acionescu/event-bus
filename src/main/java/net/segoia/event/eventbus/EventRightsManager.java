package net.segoia.event.eventbus;

import net.segoia.event.conditions.Condition;

/**
 * Manages what events should be allowed
 * 
 * @author adi
 *
 */
public class EventRightsManager {
    private Condition eventAllowedCondition;

    public EventRights getEventRights(EventContext eventContext) {
	return new EventRights(eventAllowedCondition.test(eventContext));
    }

    /**
     * @return the eventAllowedCondition
     */
    public Condition getEventAllowedCondition() {
	return eventAllowedCondition;
    }

    /**
     * @param eventAllowedCondition
     *            the eventAllowedCondition to set
     */
    public void setEventAllowedCondition(Condition eventAllowedCondition) {
	this.eventAllowedCondition = eventAllowedCondition;
    }

}
