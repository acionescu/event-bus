package net.segoia.event.eventbus.util;

import org.apache.log4j.Logger;

import net.segoia.event.eventbus.peers.util.EventNodeLogger;
import net.segoia.util.logging.Log4jLogger;

public class DefaultEventNodeLogger extends Log4jLogger implements EventNodeLogger {

    public DefaultEventNodeLogger() {
	super(Logger.getLogger("EBUSVM"));
    }

}
