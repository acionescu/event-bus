package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;

public class DefaultEventBuilder extends CustomEventBuilder{
    
    
    
    public DefaultEventBuilder() {
	super();
    }

    public DefaultEventBuilder(EventBuilderContext context) {
	super(context);
    }

    public SystemEventBuilder system() {
	return new SystemEventBuilder(newContext(1));
    }
    
    public AppEventBuilder app() {
	return new AppEventBuilder(newContext(1));
    }
    
    public DefaultEventBuilder spawnFrom( EventContext ec) {
	return spawnFrom(ec.event());
    }
    
    public DefaultEventBuilder spawnFrom ( Event event) {
	EventBuilderContext c = newContext(1);
	c.setCauseEvent(event);
	return new DefaultEventBuilder(c);
    }
    
    
}
