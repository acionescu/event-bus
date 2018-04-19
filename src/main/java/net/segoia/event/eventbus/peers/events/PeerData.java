package net.segoia.event.eventbus.peers.events;

public class PeerData {
    private byte[] data;

    public PeerData() {
	super();
    }

    public PeerData(byte[] data) {
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
