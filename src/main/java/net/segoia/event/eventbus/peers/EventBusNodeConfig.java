package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.LooseEventMatchCondition;
import net.segoia.event.eventbus.constants.Events;

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
     * The default condition that will use for a peering request
     */
    private Condition defaultRequestedEvents = LooseEventMatchCondition.build(Events.SCOPE.EBUS,Events.CATEGORY.PEER);
    

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

    /**
     * @return the defaultRequestedEvents
     */
    public Condition getDefaultRequestedEvents() {
        return defaultRequestedEvents;
    }

    /**
     * @param defaultRequestedEvents the defaultRequestedEvents to set
     */
    public void setDefaultRequestedEvents(Condition defaultRequestedEvents) {
        this.defaultRequestedEvents = defaultRequestedEvents;
    }
    
}
