/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    private boolean printAsJson;

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

	String out = null;
	if(printAsJson) {
	    out = ec.getEvent().toJson();
	}
	else {
	    out = ec.getEvent().toString();
	}
	
	return logger.trace(etc.getLoggingLevel(), out, null);
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
