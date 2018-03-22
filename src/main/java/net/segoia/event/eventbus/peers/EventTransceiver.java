package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

public interface EventTransceiver {
    void init();
    void sentEvent(Event event);
    void receiveEvent(Event event);
    void setActive(EventRelay relay);
    void terminate();
    /**
     * 
     * @return - The communication channel type ( e.g. ws, wss, http, https, etc.. )
     */
    String getChannel();
    
}
