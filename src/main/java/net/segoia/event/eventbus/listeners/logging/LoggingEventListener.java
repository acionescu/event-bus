package net.segoia.event.eventbus.listeners.logging;

import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventListener;
import net.segoia.event.eventbus.EventTypeConfig;
import net.segoia.util.logging.Logger;
import net.segoia.util.logging.LoggerFactory;
import net.segoia.util.logging.MasterLogManager;

public class LoggingEventListener implements EventListener {
    private Logger logger;
    private LoggerFactory loggerFactory;
    private String defaultLoggerName = LoggingEventListener.class.getSimpleName();

    public LoggingEventListener() {
	super();
    }

    public LoggingEventListener(Logger logger) {
	super();
	this.logger = logger;
    }

    
    @Override
    public void init() {
	if(loggerFactory != null) {
	    logger = MasterLogManager.getLogger(defaultLoggerName, loggerFactory);
	}
	else {
	    logger = MasterLogManager.getLogger(defaultLoggerName);
	}
	
    }

    
    
    @Override
    public void onEvent(EventContext ec) {
	log(ec);
    }

    protected boolean log(EventContext ec) {
	EventTypeConfig etc = ec.getConfigForEventType(true);

	if (!etc.isLoggingOn()) {
	    return false;
	}

	return logger.trace(etc.getLoggingLevel(), ec.getEvent(), null);
    }

    
    @Override
    public void terminate() {
	// TODO Auto-generated method stub
	
    }

    /**
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * @return the loggerFactory
     */
    public LoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    /**
     * @return the defaultLoggerName
     */
    public String getDefaultLoggerName() {
        return defaultLoggerName;
    }

    /**
     * @param logger the logger to set
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * @param loggerFactory the loggerFactory to set
     */
    public void setLoggerFactory(LoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    /**
     * @param defaultLoggerName the defaultLoggerName to set
     */
    public void setDefaultLoggerName(String defaultLoggerName) {
        this.defaultLoggerName = defaultLoggerName;
    }

}
