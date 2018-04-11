package net.segoia.event.eventbus.peers.events.session;

public class SessionInfo {
    private String sessionId;
    private SessionKeyData sessionData;

    public SessionInfo(String sessionId, SessionKeyData sessionData) {
	super();
	this.sessionId = sessionId;
	this.sessionData = sessionData;
    }

    public SessionInfo() {
	super();
    }

    public String getSessionId() {
	return sessionId;
    }

    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }

    public SessionKeyData getSessionData() {
	return sessionData;
    }

    public void setSessionData(SessionKeyData sessionData) {
	this.sessionData = sessionData;
    }

}
