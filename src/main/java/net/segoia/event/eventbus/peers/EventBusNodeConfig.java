package net.segoia.event.eventbus.peers;

public class EventBusNodeConfig {
    /**
     * If this is enabled, all the received events will be forwarded 
     */
    boolean autoRelayEanbled;

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

}
