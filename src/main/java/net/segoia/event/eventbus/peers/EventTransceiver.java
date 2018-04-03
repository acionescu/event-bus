package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

/**
 * Handles the transmission of events over a particular communication channel
 * @author adi
 *
 */
public interface EventTransceiver {
    void start();
    void init();
    void terminate();
    void sendEvent(Event event);
    void receiveEvent(Event event);
    void setRemoteEventListener(PeerEventListener eventListener);
    
    /**
     * Called when the communication with the peer is interrupted without us initiating it
     */
    void onPeerLeaving();
    
    /**
     * 
     * @return - The communication channel type ( e.g. ws, wss, http, https, etc.. )
     */
    String getChannel();
    
}
