package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventListener;

/**
 * Handles the transmission of events over a particular communication channel
 * @author adi
 *
 */
public interface EventTransceiver {
    void start();
    void init();
    void sendEvent(Event event);
    void receiveEvent(Event event);
    void setRemoteEventListener(EventListener eventListener);
    void terminate();
    /**
     * 
     * @return - The communication channel type ( e.g. ws, wss, http, https, etc.. )
     */
    String getChannel();
    
}
