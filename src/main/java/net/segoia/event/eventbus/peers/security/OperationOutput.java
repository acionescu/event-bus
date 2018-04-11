package net.segoia.event.eventbus.peers.security;

public class OperationOutput {
    private byte[] data;
    

    public OperationOutput(byte[] data) {
	super();
	this.data = data;
    }


    public byte[] getData() {
        return data;
    }


    public byte[] getFullOutputData() {
	return getData();
    }
}
