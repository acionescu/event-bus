package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.constants.Events;

public class EbusEventsBuilder extends ScopeRestrictedEventBuilder{

    public EbusEventsBuilder(EventBuilderContext context) {
	super(context, Events.SCOPE.EBUS);
    }
    
    
    public EbusPeerCategoryEventBuilder peer() {
	return new EbusPeerCategoryEventBuilder(context);
    }

}
