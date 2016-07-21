package net.segoia.event.eventbus.builders;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;

public class DefaultEventBuilder extends CustomEventBuilder{
    
    public SystemEventBuilder system() {
	return new SystemEventBuilder(context);
    }
    
    public AppEventBuilder app() {
	return new AppEventBuilder(context);
    }
    
    public DefaultEventBuilder spawnFrom( EventContext ec) {
	return spawnFrom(ec.event());
    }
    
    public DefaultEventBuilder spawnFrom ( Event event) {
	context.setCauseEvent(event);
	return this;
    }
    
    
}
