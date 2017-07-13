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
package net.segoia.event.eventbus.constants;

import net.segoia.event.eventbus.builders.DefaultEventBuilder;

public class Events {
    
    private static DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
    
    public static final class SCOPE {
	public static String SYSTEM="SYSTEM";
	public static String APP="APP";
	/* events concerning the bus itself */
	public static String EBUS="EBUS";
	
	public static String PEER="PEER";
    }
    
    
    public static final class CATEGORY {
	public static final String MESSAGE="MESSAGE";
	public static final String ERROR="ERROR";
	public static final String ALERT="ALERT";
	public static final String EVENT="EVENT";
	public static final String ACTION="ACTION";
	public static final String REQUEST="REQUEST";
	public static final String SESSION="SESSION";
	public static final String EXECUTING="EXECUTING";
	public static final String EXECUTED="EXECUTED";
	public static final String USER="USER";
	public static final String RESPONSE="RESPONSE";
	public static final String PEER="PEER";
	public static final String STATUS="STATUS";
    }
    
    public static final class ACTIONS {
	public static final String INITIALIZING="INITIALIZING";
	public static final String INITIALIZED="INITIALIZED";
	public static final String TERMINATING="TERMINATING";
	public static final String TERMINATED="TERMINATED";
	public static final String EXECUTING="EXECUTING";
	public static final String EXECUTED="EXECUTED";
	public static final String INIT="INIT";
	public static final String AUTHENTICATED = "AUTHENTICATED";
	public static final String LOGIN="LOGIN";
	public static final String LOGOUT="LOGOUT";
	public static final String VISIT="VISIT";
	public static final String CHANGED="CHANGED";
	public static final String NEW="NEW";
	public static final String REMOVED="REMOVED";
	public static final String UPDATED="UPDATED";
	public static final String CONNECTED="CONNECTED";
	public static final String AUTH="AUTH";
	public static final String ILLEGAL_ACTION="ILLEGAL_ACTION";
	public static final String INVALID_VALUE="INVALID_VALUE";
    }
      
    public static DefaultEventBuilder builder() {
	return eventBuilder;
    }

}
