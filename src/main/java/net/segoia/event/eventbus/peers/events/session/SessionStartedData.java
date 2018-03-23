package net.segoia.event.eventbus.peers.events.session;

import net.segoia.event.eventbus.peers.events.SessionInfo;

public class SessionStartedData {
    private SessionInfo sessionInfo;

    public SessionStartedData() {
	super();
	// TODO Auto-generated constructor stub
    }

    public SessionStartedData(SessionInfo sessionInfo) {
	super();
	this.sessionInfo = sessionInfo;
    }

    public SessionInfo getSessionInfo() {
	return sessionInfo;
    }

    public void setSessionInfo(SessionInfo sessionInfo) {
	this.sessionInfo = sessionInfo;
    }

}
