package net.segoia.event.eventbus.peers.security;

import java.util.Map;

import net.segoia.event.eventbus.peers.EventNode;
import net.segoia.event.eventbus.peers.PeerContext;

/**
 * Defines the generic security policy that governs the functioning of an {@link EventNode}
 * 
 * @author adi
 *
 */
public class EventNodeSecurityPolicy {
    private PeerBindSecurityPolicy peerBindPolicy;

    private Map<String, PeerChannelSecurityPolicy> channelsSecurity;

    public PeerBindSecurityPolicy getPeerBindPolicy() {
	return peerBindPolicy;
    }

    public void setPeerBindPolicy(PeerBindSecurityPolicy peerBindPolicy) {
	this.peerBindPolicy = peerBindPolicy;
    }

    public Map<String, PeerChannelSecurityPolicy> getChannelsSecurity() {
	return channelsSecurity;
    }

    public void setChannelsSecurity(Map<String, PeerChannelSecurityPolicy> channelsSecurity) {
	this.channelsSecurity = channelsSecurity;
    }
    
    public PeerChannelSecurityPolicy getChannelPolicy(String channel) {
	return channelsSecurity.get(channel);
    }

}
