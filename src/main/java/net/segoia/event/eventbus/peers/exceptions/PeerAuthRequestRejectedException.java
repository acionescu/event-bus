package net.segoia.event.eventbus.peers.exceptions;

import net.segoia.event.eventbus.peers.vo.auth.PeerAuthRejected;

public class PeerAuthRequestRejectedException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -4990826032432163727L;
    private PeerAuthRejected authRejectedData;

    public PeerAuthRequestRejectedException(PeerAuthRejected authRejectedData) {
	super(authRejectedData.getReason().getMessage());
	this.authRejectedData = authRejectedData;
    }

    public PeerAuthRejected getAuthRejectedData() {
	return authRejectedData;
    }

}
