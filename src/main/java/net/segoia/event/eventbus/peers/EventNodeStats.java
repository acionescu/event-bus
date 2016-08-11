package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.EventListener;

public class EventNodeStats implements EventListener{
    private long eventsHandledCount;

    @Override
    public void onEvent(EventContext ec) {
	eventsHandledCount++;
    }

    /**
     * @return the eventsHandledCount
     */
    public long getEventsHandledCount() {
        return eventsHandledCount;
    }



    @Override
    public void init() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void terminate() {
	// TODO Auto-generated method stub
	
    }
    
    
}
