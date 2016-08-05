package net.segoia.event.eventbus.test;

import net.segoia.event.eventbus.CustomEvent;
import net.segoia.event.eventbus.EventType;
import net.segoia.event.eventbus.test.CustomTestEvent.Data;

@EventType(value = "TEST:TEST:EVENT")
public class CustomTestEvent extends CustomEvent<Data> {
    
    public CustomTestEvent(String prop) {
	super(CustomTestEvent.class);
	this.data = new Data();
	this.data.prop = prop;
    }

    class Data {
	private String prop;

	/**
	 * @return the prop
	 */
	public String getProp() {
	    return prop;
	}
    }
    
}
