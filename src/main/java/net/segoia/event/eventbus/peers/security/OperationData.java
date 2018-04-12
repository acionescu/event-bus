package net.segoia.event.eventbus.peers.security;

public class OperationData {
    private byte[] data;

    public OperationData(byte[] data) {
	super();
	this.data = data;
    }
    
    /**
     * Provides a way to convert complex data structures to a single byte array
     * <br>
     * Override to implement for a custom data object
     * @return
     */
    public byte[] getFullData() {
	return getData();
    }
    

    public byte[] getData() {
	return data;
    }

    public void setData(byte[] data) {
	this.data = data;
    }

}
