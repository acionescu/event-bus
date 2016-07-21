package net.segoia.event.eventbus.constants;

import net.segoia.event.eventbus.builders.DefaultEventBuilder;

public class Events {
    
    private static DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
    
    public static final class SCOPE {
	public static String SYSTEM="SYSTEM";
	public static String APP="APP";
    }
    
    
    public static final class CATEGORY {
	public static final String MESSAGE="MESSAGE";
	public static final String ERROR="ERROR";
	public static final String ALERT="ALERT";
	public static final String EVENT="EVENT";
	public static final String ACTION="ACTION";
	public static final String EXECUTING="EXECUTING";
	public static final String EXECUTED="EXECUTED";
    }
    
    public static final class ACTIONS {
	public static final String INITIALIZING="INITIALIZING";
	public static final String INITIALIZED="INITIALIZED";
	public static final String TERMINATING="TERMINATING";
	public static final String TERMINATED="TERMINATED";
	public static final String EXECUTING="EXECUTING";
	public static final String EXECUTED="EXECUTED";
    }
      
    public static DefaultEventBuilder builder() {
	return eventBuilder;
    }

}
