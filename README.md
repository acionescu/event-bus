#Intro

This is a Java event bus framework, to promote event driven programming.

The purpose of this framework is to provide an easy way to generate and process events. 

It is encouraged to use only events as a way of communication between in app processes and third party systems.

These, through their format, can be logged, acted upon, analyzed, distributed and so on..

Events should be used to transmit information in real time from important parts of the application.

Then, any party concerned can register one or more generic or specialized listeners to further process those events.

#Why

* We live in the age of big data, when we want to collect events about everything, to better understand certain aspects of
the system, even how the system itself is running : 

	* tech driven stuff ( monitoring, performance analysis, debugging, etc )
	* business driven stuff ( analytics )
	
* Events are structured, actionable, storable, distributable, generally more *able then logs 

* If you spend some time capturing information in a relevant point of the application, make sure you're doing it in the most generic and useful way possible

* You can always generate logs from events then the other way around

* The system gets to be more decoupled when using an event driven approach

* More flexibility and control without altering the existing code ( just add more listeners on your events, or tweak the configuration file ) 

* Be prepared for scaling ( an event driven architecture is easier to scale )

* It simply is much more elegant 
 
 #How does an event needs to look like ? 
 
 Well, let's look at some desirable characteristics:
 
 * uniquely defined
 * able to carry information in a structured, flexible and easily accessible manner
 * friendly with analytics systems
 * easy to be filtered from multiple perspectives: logging, analytics, processing, security
 * support for security : we probably want to have a way to control access of different parties to our events
 * easy to be distributed in a cluster / easy to be transmitted over the network
 * persistable 
 * restorable 
 * trackable 
 	* it would be very useful to track an event's passing through the system
 	* it would also be useful to see what generated an event ( maybe another event ) and what events were generated from one event
 * immutable once posted to the bus or restored from storage
 
 
 ##What kind of information do we want to transmit ? 
 
 * unique event type, maybe built as a combination of a `scope`, `category`, `name`
 * current process information ( meant for technical staff ) 
	  * what component
	  * what thread
	  * process context parameters ( id, agent ( who is running the process ), others )
	  * system resources allocation/availability
 * app context information ( meant for app logic and product team )
	  * client/user info
	  * session
	  * user agent
	  * current app state
 
 
 ##Who would be interested in a certain event ? 
 
 * The application itself - maybe one component needs to notify another component of a certain event
 * A developer, for debugging and verification purposes
 * A sysadmin, to monitor the correct functioning and performance of the application, or to detect security issues
 * A product manager, in order to draw insight from user's behavior and the success of a certain feature or the app as a whole
 
 
 ##How would we control logging of an event ? 
 
 This actually leads to another, more broader question, namely what parties do we want to control what information ? 
 
 Well, the main event information can only be controlled by the developer, from the code. Only there this kind of info 
 is available and can be added to the event. So this cannot be debated. 
 
 However, the meta information, which specifies how an event should be treated and further processed through the system, or if an event should be fired at all, can and should be configurable from outside the code ( e.g. from a configuration file ). 
 
 I also see the usefulness of a sysadmin being able to request for a particular event more informations about the state of the system when this event was generated. The way in which this could be done should be further discussed, however I see two possible approaches: either adding more info to the original event, or generate another event with additional data that can be directly linked with the original one.
 
 So the extra feature here would be the ability to gather more info for and event
 
 
## But, let's make a list with the aspects that we want to control from an external configuration file :

- What events are allowed to be posted to the bus. Only if an event is allowed for posting, the developer should bother to fully build the event

- Logging level

- Gather more runtime info for a particular event

- Listeners and their configuration

- The whole bus configuration 


So by now, it's clear that we need an external, static configuration, that offers a certain level of control.
The limitation is that it's static, and will remain the same for the whole operation of the application.

We may want a dynamic mechanism as well, through which some components can alter the way in which an event is processed during runtime.

In order to allow for this feature, we may structure an event like this :  
 
 * the main body, which bears the main information and which is closed for modification after posting to the bus
 * a header, that would hold the meta information which controls how an event should be treated by various components in 
 a system, and that can be altered by various listeners
 * it may also be used to track the passing of an event through the system and its correlation with other events
 
 The header may consist of :
 
 * a map holding various parameters 
 * a set of tags - this may seem redundant, but they may prove useful in different ways:
   * to mark and event. Then these marks/tags can by associated with a certain functionality
   * to easily filter an event
   * to provide a way to handle security, for example tags may only be writable from code providing the developer the
   ability to ensure that certain functionality and data is not exposed to unprivileged users ( e.g. we can mark an event
   as private and thus we prevent outside modification of header params ) 
   

## Now coming back to the original question :  

How would we control logging of an event ? 

With the above structure at hand, we can do this : 

* logging can be done via a specialized EventListener that will log events according to the configuration file
* we can control the logging level of a particular event type by setting in the configuration file a parameter "logLevel:INFO" for that event
* we can also control the logging of all events marked with a certain tag, or in a specific category
* this kind of system allows to selectively enable logging and logging level for certain kinds of events, so it's very flexible
* it may further be used with a standard logging system or on its own


#How would the code look like ?

Instead of : 

```
private static Logger logger = Logger.getLogger(MyClass.class.getName());
```

you will have : 

```
private static DefaultComponentEventBuilder eventBuilder = EBus.getComponentEventBuilder(MyClass.class.getSimpleName());

```

Then, instead of : 

```
logger.info("...");
```

or

```
if(logger.isDebugEnabled()){
      logger.debug("...");
}
```

you can use : 

```
 EventHandle eh = EBus.getHandle(eventBuilder.executing().name("some operation").build());
	if (eh.isAllowed()) {
	    eh.addParam("p1","v1").addParam("p2","v2").post();
	}
```

or, in a more generic way : 

```
EventHandle eh = Events.builder().app().user().login().getHandle();
	    if (eh.isAllowed()) {
        		eh.addParam("uid", currentUser.getUserId());
        		eh.addParam("uname", currentUser.getUsername());
        		eh.post();
	    }
```

You could also spawn an event from another event, which will allow later to inspect a certain sequence of events: 

```
EventHandle eh = Events.builder().spawnFrom(causeEvent).scope("my event scope").category(" a category ").name("an event").getHandle();
	if (eh.isAllowed()) {
	    eh.post();
	}
```

Where `causeEvent` is a previous event that, logically, was the cause of the current event being triggered.


#How would the configuration look like ? 

The configuration will be automatically loaded from a file named "ebus.json" placed in the classpath. 

Here's an example configuration that registers a listener that logs received events using log4j. 
This also disallows some events from being propagated through the bus.

```
{
	"busClassName": "net.segoia.event.eventbus.FilteringEventBus",
	"defaultEventConfig": {
		"loggingOn": true,
		"loggingLevel": "INFO"
	},
	"eventRightsManager": {
		"eventAllowedCondition": {
			"id": "eventsAllowedCond",
			"ctype": "not",   /* the blacklisted event types */
			"conditions": [
				{
					"id": "bannedEventsCond",
					"ctype": "or",
					"conditions": [
						{
							"id": "bannedEvent1",
							"et": "BANNED::"   /* disables the event with the scope BANNED */
						},
						{
							"id": "bannedEvent2",
							"et": ":EXECUTING:"  /* disables the event with the category "EXECUTING" */
						},
						{
							"id": "bannedEvent3",
							"et": "COMP1:OPERATION:OPERATION_X"
						}
					]
				}
			]
		}
	},
	"listeners": {
		"list1": { /* the logging listener */
			"instance": {
				"className": "net.segoia.event.eventbus.listeners.logging.LoggingEventListener",
				"loggerFactory": {  /* we delegate logging to log4j */
					"className": "net.segoia.util.logging.Log4jLoggerFactory"
				}
			},
			"priority": 0   /* it helps to call the logging listener first so we can see the events in the order in which they are triggered */
		},
		"list2" : {
			"instance" : {
				"className" : "net.segoia.event.eventbus.test.TestEventListener",
				"echoOn":true,
				"listenerKey" : "userLoginEchoKey"
			},
			"priority" : 10,
			"condition" : {    /* we can register a listener to be called only for events satisfying a certain condition */
				"id" : "userLoginCond",
				"et" :"APP:USER:LOGIN"  
				},
			"condPriority" : 50
		}
	}
}
```

Of course, many more listeners doing different stuff can be added. We may add a listener that stores the events in a database, or that sends an email when a particular event happens, or who knows.
