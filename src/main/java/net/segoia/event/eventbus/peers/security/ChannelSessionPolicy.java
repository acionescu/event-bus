package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.CommStrategy;
import net.segoia.event.eventbus.peers.events.session.KeyDef;

public class ChannelSessionPolicy {
    /**
     * The type of key to use as session keys
     */
    private KeyDef sessionKeyDef;

    /**
     * The strategy used to send the session key to the peer
     */
    private CommStrategy sessionCommStrategy;

    public KeyDef getSessionKeyDef() {
	return sessionKeyDef;
    }

    public void setSessionKeyDef(KeyDef sessionKeyDef) {
	this.sessionKeyDef = sessionKeyDef;
    }

    public CommStrategy getSessionCommStrategy() {
	return sessionCommStrategy;
    }

    public void setSessionCommStrategy(CommStrategy sessionCommStrategy) {
	this.sessionCommStrategy = sessionCommStrategy;
    }

}
