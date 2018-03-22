package net.segoia.event.eventbus.peers.events.auth;

public class PeerAuthRejected {
    private String reason;

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

}
