package net.segoia.event.eventbus.peers.events;

public class PeerInfo {
    private String peerId;

    public PeerInfo(String peerId) {
	super();
	this.peerId = peerId;
    }

    /**
     * @return the peerId
     */
    public String getPeerId() {
	return peerId;
    }

    /**
     * @param peerId
     *            the peerId to set
     */
    public void setPeerId(String peerId) {
	this.peerId = peerId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("PeerInfo [");
	if (peerId != null)
	    builder.append("peerId=").append(peerId);
	builder.append("]");
	return builder.toString();
    }

}
