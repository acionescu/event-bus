package net.segoia.event.eventbus.peers.exceptions;

import net.segoia.event.eventbus.peers.PeerContext;

public class PeerRequestRejectedException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = -1824051863240713530L;
    
    private PeerContext peerContext;

    public PeerRequestRejectedException(String message, Throwable cause, PeerContext peerContext) {
	super(message, cause);
	this.peerContext = peerContext;
    }

    public PeerRequestRejectedException(String message, PeerContext peerContext) {
	super(message);
	this.peerContext = peerContext;
    }
    
    

}
