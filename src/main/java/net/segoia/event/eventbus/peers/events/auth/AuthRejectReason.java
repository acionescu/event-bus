package net.segoia.event.eventbus.peers.events.auth;

public class AuthRejectReason<D> {
    private String message;

    private D data;

    public AuthRejectReason() {
	super();
	// TODO Auto-generated constructor stub
    }

    public AuthRejectReason(String message) {
	super();
	this.message = message;
    }

    public AuthRejectReason(String message, D data) {
	super();
	this.message = message;
	this.data = data;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public D getData() {
	return data;
    }

    public void setData(D data) {
	this.data = data;
    }

}
