package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.session.KeyDef;

public class ChannelSessionPolicy {
    /**
     * The type of key to use as session keys
     */
    private KeyDef sessionKeyDef;

    public KeyDef getSessionKeyDef() {
        return sessionKeyDef;
    }

    public void setSessionKeyDef(KeyDef sessionKeyDef) {
        this.sessionKeyDef = sessionKeyDef;
    }
    
    
}
