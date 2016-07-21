package net.segoia.event.eventbus;

public interface EventListener {
    void onEvent(EventContext ec);
    
    void init();
    
    void terminate();
}
