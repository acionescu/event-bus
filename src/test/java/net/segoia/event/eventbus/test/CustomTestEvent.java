package net.segoia.event.eventbus.test;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventType;

@EventType(value="TEST:TEST:EVENT")
public class CustomTestEvent extends Event{
    private String prop;

    public CustomTestEvent(String prop) {
	super(CustomTestEvent.class);
	this.prop = prop;
    }

    /**
     * @return the prop
     */
    public String getProp() {
        return prop;
    }
}
