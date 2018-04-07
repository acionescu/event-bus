package net.segoia.event.eventbus.peers.security;

public class CommOperationException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = -7309100919410242075L;
    
    private CommOperationContext context;
    
    

   

    public CommOperationException(String message, Throwable cause, CommOperationContext context) {
	super(message, cause);
	this.context = context;
    }

    public CommOperationException() {
	super();
	// TODO Auto-generated constructor stub
    }

    public CommOperationException(String message, Throwable cause, boolean enableSuppression,
	    boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
    }

    public CommOperationException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    public CommOperationException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    public CommOperationException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }
    
    

}
