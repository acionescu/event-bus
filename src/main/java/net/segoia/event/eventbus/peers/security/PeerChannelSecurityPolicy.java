package net.segoia.event.eventbus.peers.security;

/**
 * Defines the security policy for binding with a node via a certain channel ( e.g. http, https, ws, wss, etc.. )
 * 
 * @author adi
 *
 */
public class PeerChannelSecurityPolicy {
    /**
     * The communicatin policy for this channel
     */
    private ChannelCommunicationPolicy communicationPolicy;

    public ChannelCommunicationPolicy getCommunicationPolicy() {
	return communicationPolicy;
    }

    public void setCommunicationPolicy(ChannelCommunicationPolicy communicationPolicy) {
	this.communicationPolicy = communicationPolicy;
    }

}
