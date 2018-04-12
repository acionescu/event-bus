package net.segoia.event.eventbus.peers.events.session;

public class SessionKey {
    private String key;
    private KeyDef keyDef;

    public SessionKey(String key, KeyDef keyDef) {
	super();
	this.key = key;
	this.keyDef = keyDef;

    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public KeyDef getKeyDef() {
	return keyDef;
    }

    public void setKeyDef(KeyDef keyDef) {
	this.keyDef = keyDef;
    }

}
