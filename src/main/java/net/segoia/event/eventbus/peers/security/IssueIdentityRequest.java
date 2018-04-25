package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.IdentityType;

public class IssueIdentityRequest {
    private IdentityType identityType;
    private long duration;

    public IssueIdentityRequest(IdentityType identityType) {
	super();
	this.identityType = identityType;
    }

    public IdentityType getIdentityType() {
	return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
	this.identityType = identityType;
    }

    public long getDuration() {
	return duration;
    }

    public void setDuration(long duration) {
	this.duration = duration;
    }

}
