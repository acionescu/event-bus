package net.segoia.event.eventbus.peers.events;

public class SessionInfo {
    private String sessionId;
    /**
     * Can be used for reconnection
     */
    private String securityToken;

    public SessionInfo(String sessionId, String securityToken) {
	super();
	this.sessionId = sessionId;
	this.securityToken = securityToken;
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

    public String getSecurityToken() {
	return securityToken;
    }

    public void setSecurityToken(String securityToken) {
	this.securityToken = securityToken;
    }

}
