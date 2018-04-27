package net.segoia.event.eventbus.peers.events;

public class RequestRejectReason<D> {
    private String message;

    private D data;

    public RequestRejectReason() {
	super();
	// TODO Auto-generated constructor stub
    }

    public RequestRejectReason(String message) {
	super();
	this.message = message;
    }

    public RequestRejectReason(String message, D data) {
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
