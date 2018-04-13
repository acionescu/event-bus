package net.segoia.event.eventbus.peers.events.session;

public class SessionKey {
    private String sessionId;
    private byte[] keyBytes;
    private KeyDef keyDef;

    public SessionKey(String sessionId, byte[] keyBytes, KeyDef keyDef) {
	super();
	this.sessionId = sessionId;
	this.keyBytes = keyBytes;
	this.keyDef = keyDef;
    }

    public byte[] getKeyBytes() {
	return keyBytes;
    }

    public void setKeyBytes(byte[] keyBytes) {
	this.keyBytes = keyBytes;
    }

    public KeyDef getKeyDef() {
	return keyDef;
    }

    public void setKeyDef(KeyDef keyDef) {
	this.keyDef = keyDef;
    }

    public String getSessionId() {
	return sessionId;
    }

    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }

}
