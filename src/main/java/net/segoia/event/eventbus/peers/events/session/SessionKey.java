package net.segoia.event.eventbus.peers.events.session;

public class SessionKey {
    private String key;
    private KeyDef keyInfo;

    public SessionKey(String key, KeyDef keyInfo) {
	super();
	this.key = key;
	this.keyInfo = keyInfo;

    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public KeyDef getKeyInfo() {
	return keyInfo;
    }

    public void setKeyInfo(KeyDef keyInfo) {
	this.keyInfo = keyInfo;
    }

}
