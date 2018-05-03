package net.segoia.event.eventbus.peers.security;

import java.util.List;

import net.segoia.event.eventbus.peers.core.IdentitiesManager;
import net.segoia.event.eventbus.peers.vo.auth.NodeAuth;
import net.segoia.event.eventbus.peers.vo.security.EventNodeSecurityPolicy;

public class EventNodeSecurityConfig {
    private NodeAuth nodeAuth;
    private EventNodeSecurityPolicy securityPolicy;

    /**
     * Private identity data loaders
     */
    private List<PrivateIdentityDataLoader<?>> identityLoaders;

    private IdentitiesManager identitiesManager= new DefaultIdentitiesManager();

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

    public IdentitiesManager getIdentitiesManager() {
	return identitiesManager;
    }

    public void setIdentitiesManager(IdentitiesManager identitiesManager) {
	this.identitiesManager = identitiesManager;
    }

}
