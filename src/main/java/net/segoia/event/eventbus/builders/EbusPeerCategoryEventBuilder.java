package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.constants.Events;

public class EbusPeerCategoryEventBuilder extends CategoryRestrictedEventBuilder{

    public EbusPeerCategoryEventBuilder(EventBuilderContext context) {
	super(context, Events.CATEGORY.PEER);
    }
    
    public EventBuilder newPeer() {
	return super.name(Events.ACTIONS.NEW);
	
    }
    
    public EventBuilder peerRemoved() {
	return super.name(Events.ACTIONS.REMOVED);
    }
    
    public EventBuilder init() {
	return super.name(Events.ACTIONS.INIT);
    }

}
