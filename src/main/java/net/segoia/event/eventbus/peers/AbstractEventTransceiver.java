package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.EventListener;

public abstract class AbstractEventTransceiver implements EventTransceiver {
   
    private EventListener remoteEventListener;

   
    public void setRemoteEventListener(EventListener remoteEventListener) {
	this.remoteEventListener=remoteEventListener;
    }

    protected EventListener getRemoteEventListener() {
	return remoteEventListener;
    }
   
    
}
