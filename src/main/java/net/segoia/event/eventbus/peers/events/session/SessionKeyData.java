package net.segoia.event.eventbus.peers.events.session;

import net.segoia.event.eventbus.peers.comm.CommStrategy;
import net.segoia.event.eventbus.peers.security.ChannelSessionPolicy;

public class SessionKeyData {
    /**
     * The base 64 encoded session key, after it was processed by the operations specified in the
     * {@link ChannelSessionPolicy} <br>
     * The receiver will have to apply the inverse operations, and decode the result in order to obtain the session key
     */
    private String sessionToken;

    /**
     * Defines the session key type
     */
    private KeyDef keyDef;

    /**
     * The strategy used to process the session key before sending it to the peer <br>
     * Here are defined the operations used to process the session key, so the receiver will have to apply the inverse
     * of these operations in order to obtain it
     */
    private CommStrategy sessionCommStrategy;

    public SessionKeyData(String sessionToken, KeyDef keyDef, CommStrategy sessionCommStrategy) {
	super();
	this.sessionToken = sessionToken;
	this.keyDef = keyDef;
	this.sessionCommStrategy = sessionCommStrategy;
    }

    public String getSessionToken() {
	return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
	this.sessionToken = sessionToken;
    }

    public KeyDef getKeyDef() {
	return keyDef;
    }

    public void setKeyDef(KeyDef keyDef) {
	this.keyDef = keyDef;
    }

    public CommStrategy getSessionCommStrategy() {
	return sessionCommStrategy;
    }

    public void setSessionCommStrategy(CommStrategy sessionCommStrategy) {
	this.sessionCommStrategy = sessionCommStrategy;
    }

}
