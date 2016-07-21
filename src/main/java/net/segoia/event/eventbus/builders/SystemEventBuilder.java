package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.constants.Events;

public class SystemEventBuilder extends ScopeRestrictedEventBuilder{

    public SystemEventBuilder(EventBuilderContext context) {
	super(context,Events.SCOPE.SYSTEM);
    }
    
    
    public CategoryRestrictedEventBuilder message() {
	return category(Events.CATEGORY.MESSAGE);
    }
    
    public CategoryRestrictedEventBuilder error() {
	return category(Events.CATEGORY.ERROR);
    }

    public CategoryRestrictedEventBuilder alert() {
	return category(Events.CATEGORY.ALERT);
    }
    
    public CategoryRestrictedEventBuilder event() {
	return category(Events.CATEGORY.EVENT);
    }
    
}
