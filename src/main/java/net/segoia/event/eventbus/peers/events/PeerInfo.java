package net.segoia.event.eventbus.peers.events;

public class PeerInfo {
    /**
     * Local peer id
     */
    private String peerId;

    /**
     * The type of peer
     */
    private String peerType;

    /**
     * The info sent by the peer
     */
    private NodeInfo nodeInfo;

    public PeerInfo(String peerId, String peerType, NodeInfo nodeInfo) {
	super();
	this.peerId = peerId;
	this.peerType = peerType;
	this.nodeInfo = nodeInfo;
    }

    public String getPeerId() {
	return peerId;
    }

    public NodeInfo getNodeInfo() {
	return nodeInfo;
    }

    public String getPeerType() {
	return peerType;
    }

}
