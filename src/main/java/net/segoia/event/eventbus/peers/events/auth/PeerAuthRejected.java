package net.segoia.event.eventbus.peers.events.auth;

public class PeerAuthRejected {
    private AuthRejectReason reason;

    public PeerAuthRejected() {
	super();
	// TODO Auto-generated constructor stub
    }

    public PeerAuthRejected(AuthRejectReason reason) {
	super();
	this.reason = reason;
    }

    public AuthRejectReason getReason() {
	return reason;
    }

    public void setReason(AuthRejectReason reason) {
	this.reason = reason;
    }

}
