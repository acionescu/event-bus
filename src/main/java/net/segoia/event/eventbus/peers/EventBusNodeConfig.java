package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;

public class EventBusNodeConfig {
    /**
     * If this is enabled, this node will relay events from other peers as well
     */
    private boolean autoRelayEanbled;
    
    /**
     * What internal events this node is allowed to share ( forward to peers )
     * </br>
     * If left null, it will share all events
     */
    private Condition allowedSharedEvents;
    

    /**
     * @return the autoRelayEanbled
     */
    public boolean isAutoRelayEanbled() {
        return autoRelayEanbled;
    }

    /**
     * @param autoRelayEanbled the autoRelayEanbled to set
     */
    public void setAutoRelayEanbled(boolean autoRelayEanbled) {
        this.autoRelayEanbled = autoRelayEanbled;
    }

    /**
     * @return the allowedSharedEvents
     */
    public Condition getAllowedSharedEvents() {
        return allowedSharedEvents;
    }

    /**
     * @param allowedSharedEvents the allowedSharedEvents to set
     */
    public void setAllowedSharedEvents(Condition allowedSharedEvents) {
        this.allowedSharedEvents = allowedSharedEvents;
    }
    
}
