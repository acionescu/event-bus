package net.segoia.event.eventbus.peers.security;

import java.util.List;

import net.segoia.event.eventbus.peers.comm.NodeCommunicationStrategy;

public class ChannelCommunicationPolicy {
    /**
     * Supported tx strategies in the preferred order
     */
    private List<NodeCommunicationStrategy> supportedTxStrategies;

    /**
     * Supported rx strategies in preferred order ( in tx format )
     */
    private List<NodeCommunicationStrategy> supportedRxStrategies;
    
    /**
     * The policy for session management for this channel
     */
    private ChannelSessionPolicy sessionPolicy;

    public List<NodeCommunicationStrategy> getSupportedTxStrategies() {
	return supportedTxStrategies;
    }

    public void setSupportedTxStrategies(List<NodeCommunicationStrategy> supportedTxStrategies) {
	this.supportedTxStrategies = supportedTxStrategies;
    }

    public List<NodeCommunicationStrategy> getSupportedRxStrategies() {
	return supportedRxStrategies;
    }

    public void setSupportedRxStrategies(List<NodeCommunicationStrategy> supportedRxStrategies) {
	this.supportedRxStrategies = supportedRxStrategies;
    }

    public ChannelSessionPolicy getSessionPolicy() {
        return sessionPolicy;
    }

    public void setSessionPolicy(ChannelSessionPolicy sessionPolicy) {
        this.sessionPolicy = sessionPolicy;
    }

}
