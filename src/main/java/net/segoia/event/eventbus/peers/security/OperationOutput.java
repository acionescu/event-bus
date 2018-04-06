package net.segoia.event.eventbus.peers.security;

public class OperationOutput extends RawDataOperationContext{

    public OperationOutput(byte[] data) {
	super(data);
    }

    public byte[] getFullOutputData() {
	return getData();
    }
}
