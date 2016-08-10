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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("Data [");
	    if (prop != null)
		builder.append("prop=").append(prop);
	    builder.append("]");
	    return builder.toString();
	}
	
    }
    
}
