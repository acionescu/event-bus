package net.segoia.event.eventbus.peers.events.session;

import net.segoia.event.eventbus.peers.events.EncryptionInfo;

public class SessionKeyData {
    private SessionKey sessionKey;
    private EncryptionInfo encryptionInfo;

    public SessionKeyData(SessionKey sessionKey, EncryptionInfo encryptionInfo) {
	super();
	this.sessionKey = sessionKey;
	this.encryptionInfo = encryptionInfo;
    }

    public SessionKeyData(SessionKey sessionKey) {
	super();
	this.sessionKey = sessionKey;
    }

    public SessionKey getSessionKey() {
	return sessionKey;
    }

    public void setSessionKey(SessionKey sessionKey) {
	this.sessionKey = sessionKey;
    }

    public EncryptionInfo getEncryptionInfo() {
	return encryptionInfo;
    }

    public void setEncryptionInfo(EncryptionInfo encryptionInfo) {
	this.encryptionInfo = encryptionInfo;
    }

}
