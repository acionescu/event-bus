package net.segoia.event.eventbus.peers.exceptions;

import net.segoia.event.eventbus.peers.PeerContext;

public class PeerRequestHandlingException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = -8621733881962424286L;
    
    private PeerContext peerContext;

    public PeerRequestHandlingException(String message, PeerContext peerContext) {
	super(message);
	this.peerContext = peerContext;
    }

    public PeerRequestHandlingException(String message, Throwable cause, PeerContext peerContext) {
	super(message, cause);
	this.peerContext = peerContext;
    }
    
    

}
