I claim that one should always generate events instead of logging

Here's why:

* We live in the age of big data, when we want to collect events about everything to better understand certain aspects of
the system : 

	* tech driven stuff ( monitoring, performance analysis, debugging, etc )
	* business driven stuff ( analytics )
	
* Events are structured, actionable, storable, distributable, generally more *able then logs 

* You can always generated logs from events then the other way around
	
* Allows you more decoupling through event driven programming

* More flexibility and control without altering the existing code ( just add more listeners on your events, or tweak the configuration file ) 

* Be prepared for scaling ( an event driven architecture is easier to scale )


