package net.segoia.event.eventbus.peers.events.session;

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

    public SessionKeyData(String sessionToken, KeyDef keyDef) {
	super();
	this.sessionToken = sessionToken;
	this.keyDef = keyDef;
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

}
