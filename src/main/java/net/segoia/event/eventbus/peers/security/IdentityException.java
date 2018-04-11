package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;

public class IdentityException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1524357609581246839L;

    private NodeIdentity<?> nodeIdentity;

    public IdentityException(String message, Throwable cause, NodeIdentity<?> nodeIdentity) {
	super(message, cause);
	this.nodeIdentity = nodeIdentity;
    }

}
