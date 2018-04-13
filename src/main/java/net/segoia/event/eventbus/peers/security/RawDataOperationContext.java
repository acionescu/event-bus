package net.segoia.event.eventbus.peers.security;

public class RawDataOperationContext<D extends OperationDef> extends OperationContext<D> {
    private byte[] data;

    

    public RawDataOperationContext(D opDef, byte[] data) {
	super(opDef);
	this.data = data;
    }

    public byte[] getData() {
	return data;
    }

    public void setData(byte[] data) {
	this.data = data;
    }
}