package net.segoia.event.eventbus.peers.security;

public class OperationData {
    private byte[] data;

    public OperationData(byte[] data) {
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
