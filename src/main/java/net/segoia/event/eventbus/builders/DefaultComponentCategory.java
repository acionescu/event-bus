package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.constants.Events;

public class DefaultComponentCategory extends EventBuilder {

    public DefaultComponentCategory(EventBuilderContext context, String category) {
	super(context);
	category(category);
    }

    public EventBuilder initializing() {
	return super.name(Events.ACTIONS.INITIALIZING);
    }

    public EventBuilder initialized() {
	return super.name(Events.ACTIONS.INITIALIZED);
    }

    public EventBuilder terminating() {
	return super.name(Events.ACTIONS.TERMINATING);
    }

    public EventBuilder terminated() {
	return super.name(Events.ACTIONS.TERMINATED);
    }
    
    public EventBuilder executing() {
	return super.name(Events.ACTIONS.EXECUTING);
    }
    
    public EventBuilder executed() {
	return super.name(Events.ACTIONS.EXECUTED);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.builders.EventBuilder#name(java.lang.String)
     */
    @Override
    public EventBuilder name(String name) {
	return super.name(name);
    }
    
    
}
