package net.segoia.event.eventbus.test.test.logger;

import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.listeners.logging.LoggingEventListener;
import net.segoia.event.eventbus.test.TestEventTags;
import net.segoia.util.logging.Logger;

public class TestLoggingEventListener extends LoggingEventListener{

    public TestLoggingEventListener(Logger logger) {
	super(logger);
    }

    /* (non-Javadoc)
     * @see net.segoia.event.eventbus.listeners.logging.LoggingEventListener#log(net.segoia.event.eventbus.EventContext)
     */
    @Override
    protected boolean log(EventContext ec) {
	boolean logged =  super.log(ec);
	if(logged) {
	    ec.getEvent().tag(TestEventTags.LOGGED);
	}
	return logged;
    }
    
    

}
