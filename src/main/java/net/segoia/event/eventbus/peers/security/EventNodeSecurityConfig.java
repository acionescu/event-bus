package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.NodeAuth;

public class EventNodeSecurityConfig {
    private NodeAuth nodeAuth;
    private EventNodeSecurityPolicy securityPolicy;

    public NodeAuth getNodeAuth() {
	return nodeAuth;
    }

    public void setNodeAuth(NodeAuth nodeAuth) {
	this.nodeAuth = nodeAuth;
    }

    public EventNodeSecurityPolicy getSecurityPolicy() {
	return securityPolicy;
    }

    public void setSecurityPolicy(EventNodeSecurityPolicy securityPolicy) {
	this.securityPolicy = securityPolicy;
    }

}
