package net.segoia.event.eventbus.peers.security;

public class RawDataOperationContext extends OperationContext {
    private byte[] data;

    public RawDataOperationContext(byte[] data) {
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
