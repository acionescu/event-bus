package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.constants.Events;

public class DefaultComponentEventBuilder extends EventBuilder{
    
    public DefaultComponentEventBuilder(String scope) {
	this(new EventBuilderContext(),scope);
    }

    public DefaultComponentEventBuilder(EventBuilderContext context, String scope) {
	super(context);
	scope(scope);
    }
    
    protected DefaultComponentEventBuilder(EventBuilderContext context) {
	super(context);
    }
    
    public DefaultComponentEventBuilder spawnFrom ( Event event) {
	EventBuilderContext c = newContext(1);
	c.setCauseEvent(event);
	return new DefaultComponentEventBuilder(c);
    }
    
    
    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.builders.ScopeRestrictedEventBuilder#category(java.lang.String)
     */
    @Override
    public DefaultComponentCategory category(String category) {
	return new DefaultComponentCategory(newContext(1), category);
    }

    public DefaultComponentCategory message() {
	return category(Events.CATEGORY.MESSAGE);
    }
    
    public DefaultComponentCategory error() {
	return category(Events.CATEGORY.ERROR);
    }

    public DefaultComponentCategory alert() {
	return category(Events.CATEGORY.ALERT);
    }
    
    public DefaultComponentCategory event() {
	return category(Events.CATEGORY.EVENT);
    }
    
    public DefaultComponentCategory executing() {
	return category(Events.CATEGORY.EXECUTING);
    }

    public DefaultComponentCategory executed() {
	return category(Events.CATEGORY.EXECUTED);
    }
}
