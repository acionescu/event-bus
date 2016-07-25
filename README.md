This is a Java event bus framework. 

The purpose of this framework is to provide an easy way to generate and process events. 

It is encouraged to use only events as a way of communication between in app processes and third party systems.

These, through their format, can be logged, acted upon, analyzed, distributed and so on..

Events should be used to transmit information in real time from important parts of the application.

Then, any party concerned can register one or more generic or specialized listeners to further process those events.

Posting events and registering listeners can be done via the EBus instance, which by default will be a singleton for the current app.

 
 How does an event needs to look like ? 
 
 Well, let's look at some desirable characteristics:
 
 * uniquely defined
 * able to carry information in a structured, flexible and easily accessible manner
 * friendly with analytics systems
 * easy to be filtered from multiple perspectives: logging, analytics, processing, security
 * support for security : we probably want to have a way to control access of different parties to our events
 * easy to be distributed in a cluster / easy to be transmitted over the network
 * persistable - easy to persist
 * restorable - easy to restore from a persisted form
 * tracking - it would be very useful to track an event's passing through the system
 * correlation - it would also be useful to see what generated an event ( maybe another event ) and what events were generated from one event
 * immutable once posted to the bus or restored from storage
 
 
 What kind of information do we want to transmit ? 
 
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
 
 
 Who would be interested in a certain event ? 
 
 * The application itself - maybe one component needs to notify another component of a certain event
 * A developer, for debugging and verification purposes
 * A sysadmin to monitor the correct functioning and performance of the application, or to detect security issues
 * A product manager, in order to draw insight from user's behavior and the success of a certain feature or the app as a whole
 
 
 How would we control logging of an event ? 
 
 This actually leads to another, more broader question, namely what parties do we want to control what information ? 
 
 Well, the main event information can only be controlled by the developer, from the code. Only there this kind of info 
 is available and can be added to the event. So this cannot be debated. 
 
 However, the meta information, which specifies how an event should be treated and further processed through the system, or if an event should be fired at all, can and should be configurable from outside the code ( e.g. from a configuration file ). 
 
 I also see the usefulness of a sysadmin being able to request for a particular event more informations about the state of the system when this event was generated. The way in which this could be done should be further discussed, however I see two possible approaches: either adding more info to the original event, or generate another event with additional data that can be directly linked with the original one.
 
 So the extra feature here would be the ability to gather more info for and event
 
 
But, let's make a list with the aspects that we want to control from an external configuration file :

- What events are allowed to be posted to the bus. Only if an event is allowed for posting, the developer should bother to fully build the event

- Logging level

- Gather more runtime info for a particular event

- Adding listeners

- The whole bus configuration 



So by now, it's clear that we need an external, static configuration that offers a certain level of control.
The limitation of this is that it's static, and will remain the same for the whole operation of the application.

We may want a dynamic mechanism as well, through which some components can alter the way in which an event is processed during runtime.


In order to allowe for this feature as well, we may structure an event like this :  
 
 * the main body which bears the main information and which is closed for modification after posting to the bus
 * a header, that would hold the meta information which controls how an event should be treated by various components in 
 a system
 * the header may also be used to track the passing of an event through the system and its correlation with other events
 
 
 The header may consist of :
 
 * a map holding various parameters 
 * a set of tags - this may seem redundant, but they may prove useful in different ways:
   * to mark and event. Then these marks/tags can by associated with a certain functionality
   * to easily filter an event
   * to provide a way to handle security, for example tags may only be writable from code providing the developer the
   ability to ensure that certain functionality and data is no exposed to unprivileged users ( e.g. we can mark an event
   as private and thus we prevent outside modification of header params via a configuration file ) 
   

Now coming back to the original question :  

How would we control logging of an event ? 

With the above structure at hand we can do this : 

* logging can be done via a specialized EventListener that will log events according to the configuration file
* we can control the logging level of a particular event type by setting in the configuration file a parameter "logLevel:INFO" for that event
* we can also control the logging of all events marked with a certain tag
* this kind of system allows to selectively enable logging and logging level for certain kinds of events, so its very flexible
* it may further be used with a standard logging system or on its own




