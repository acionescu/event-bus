package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.constants.Events;

public class EbusEventsBuilder extends ScopeRestrictedEventBuilder{

    public EbusEventsBuilder(EventBuilderContext context) {
	super(context, Events.SCOPE.EBUS);
    }
    
    
    public ClusterCategoryEventBuilder cluster() {
	return new ClusterCategoryEventBuilder(context);
    }

}
