package net.segoia.event.eventbus.peers.security;

import java.util.List;

import net.segoia.event.eventbus.peers.events.auth.NodeAuth;

public class EventNodeSecurityConfig {
    private NodeAuth nodeAuth;
    private EventNodeSecurityPolicy securityPolicy;

    /**
     * Private identity data loaders
     */
    private List<PrivateIdentityDataLoader<?>> identityLoaders;

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

    public List<PrivateIdentityDataLoader<?>> getIdentityLoaders() {
	return identityLoaders;
    }

    public void setIdentityLoaders(List<PrivateIdentityDataLoader<?>> identityLoaders) {
	this.identityLoaders = identityLoaders;
    }

}
