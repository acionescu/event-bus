package net.segoia.event.eventbus.peers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventNodePeersRegistry {
    /**
     * keep the relays associated with peers
     */
    private Map<String, PeerManager> directPeers = new HashMap<>();

    private Map<String, PeerManager> remotePeers = new HashMap<>();

    /**
     * Nodes with which we are still negotiating peering
     */
    private Map<String, PeerManager> pendingPeers = new HashMap<>();

    /**
     * The ids of direct peers registered as agents
     */
    private Set<String> agents = new HashSet<>();
    
    
    public boolean setPendingPeerAsDirectPeer(String peerId) {
	PeerManager pm = removePeer(pendingPeers, peerId);
	if(pm == null) {
	    return false;
	}
	setPeerManager(directPeers, pm);
	return true;
    }
    

    public PeerManager getPendingPeerManager(String peerId) {
	return getPeerManger(pendingPeers, peerId);
    }

    public PeerManager getRemotePeerManager(String peerId) {
	return getPeerManger(remotePeers, peerId);
    }

    public PeerManager getDirectPeerManager(String peerId) {
	return getPeerManger(directPeers, peerId);
    }

    public PeerManager getPeerManger(Map<String, PeerManager> targetMap, String peerId) {
	return targetMap.get(peerId);

    }

    public void addAgent(String peerId) {
	agents.add(peerId);
    }

    public void setPendingPeerManager(PeerManager peerManager) {
	setPeerManager(pendingPeers, peerManager);
    }

    private void setPeerManager(Map<String, PeerManager> targetMap, PeerManager peerManager) {
	targetMap.put(peerManager.getPeerId(), peerManager);
    }

    public PeerManager removeRemotePeer(String peerId) {
	return removePeer(remotePeers, peerId);
    }

    public PeerManager removeDirectPeer(String peerId) {
	return removePeer(directPeers, peerId);
    }
    
    public PeerManager removePendingPeer(String peerId) {
	return removePeer(pendingPeers, peerId);
    }

    private PeerManager removePeer(Map<String, PeerManager> targetMap, String peerId) {
	return targetMap.remove(peerId);
    }

    public Map<String, PeerManager> getDirectPeers() {
	return directPeers;
    }

    public void setDirectPeers(Map<String, PeerManager> directPeers) {
	this.directPeers = directPeers;
    }

    public Map<String, PeerManager> getRemotePeers() {
	return remotePeers;
    }

    public void setRemotePeers(Map<String, PeerManager> remotePeers) {
	this.remotePeers = remotePeers;
    }

    public Set<String> getAgents() {
	return agents;
    }

    public void setAgents(Set<String> agents) {
	this.agents = agents;
    }

}
