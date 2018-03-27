# Considerations

Technical considerations and questions.


### What kind of events should we process on the main loop ?

The main event loop functions in a secure environment, so only events that pass the security and contract enforcement can be posted to the main loop.

### Who is allowed to process events on the event node's bus ?

Event node agents.

### What cand an event node agent can do ?

* Local events agents
    * listen on local events
    * post local events


* Remote events agents
    * Everything a local event agent can do
    * Listen to events from peers
    * Forward events to peers
    
    
    
