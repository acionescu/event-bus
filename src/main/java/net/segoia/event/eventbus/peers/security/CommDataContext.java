package net.segoia.event.eventbus.peers.security;

public class CommDataContext {
    private byte[] data;

    public CommDataContext() {
	super();
	// TODO Auto-generated constructor stub
    }

    public CommDataContext(byte[] data) {
	super();
	this.data = data;
    }

    public byte[] getData() {
	return data;
    }

    public void setData(byte[] data) {
	this.data = data;
    }

}
