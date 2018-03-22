package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.EventContext;
import net.segoia.event.eventbus.peers.events.NodeInfo;
import net.segoia.event.eventbus.peers.events.auth.PeerAuthRequest;
import net.segoia.event.eventbus.peers.events.bind.PeerBindAccepted;

/**
 * A container to keep all the informations about a peer node
 * 
 * @author adi
 *
 */
public class PeerManager {
    private PeerContext peerContext;
    private EventRelay relay;
    private String peerId;

    public PeerManager(String peerId, EventRelay relay) {

	peerContext = new PeerContext(peerId, relay);
	this.relay = relay;
	this.peerId = peerId;
    }

    public PeerManager(EventRelay relay) {
	super();
	this.relay = relay;
	peerContext = new PeerContext(relay);
    }

    public String getPeerId() {
	return peerContext.getPeerId();
    }

    public void setPeerInfo(NodeInfo peerInfo) {
	peerContext.setPeerInfo(peerInfo);
    }

    public NodeInfo getPeerInfo() {
	return peerContext.getPeerInfo();
    }

    public void start() {
	relay.bind();
    }

    public void handlePeerBindAccepted(PeerBindAccepted data) {
	peerContext.setPeerInfo(data.getNodeInfo());
    }

    public void handlePeerAuthRequest(PeerAuthRequest data) {
	peerContext.setPeerInfo(data.getNodeInfo());
    }

    public void forwardToPeer(Event event) {
	relay.onLocalEvent(new EventContext(event));
    }

    public void setInServerMode(boolean inServerMode) {
	peerContext.setInServerMode(inServerMode);
    }

    public PeerContext getPeerContext() {
	return peerContext;
    }

}
