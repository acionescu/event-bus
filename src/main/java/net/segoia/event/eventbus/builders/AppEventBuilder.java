package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.constants.Events;

public class AppEventBuilder extends ScopeRestrictedEventBuilder {

    public AppEventBuilder(EventBuilderContext context) {
	super(context, Events.SCOPE.APP);
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
}
