package net.segoia.event.eventbus.services;

import java.util.List;
import java.util.Map;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;

public class NodeIdentityProfile {
    private String identityKey;
    private NodeIdentity<?> identity;
    /**
     * service id
     */
    private Map<String, ServiceContract> serviceContracts;

    /**
     * Identity key for the parent identity
     */
    private String parentIdentityKey;

    private List<String> childIdentityKeysList;

    public NodeIdentityProfile(NodeIdentity<?> identity) {
	super();
	this.identity = identity;
    }

    public NodeIdentityProfile() {
	super();
	// TODO Auto-generated constructor stub
    }

    public NodeIdentity<?> getIdentity() {
	return identity;
    }

    public void setIdentity(NodeIdentity<?> identity) {
	this.identity = identity;
    }

    public Map<String, ServiceContract> getServiceContracts() {
	return serviceContracts;
    }

    public void setServiceContracts(Map<String, ServiceContract> serviceContracts) {
	this.serviceContracts = serviceContracts;
    }

    public String getIdentityKey() {
	return identityKey;
    }

    public void setIdentityKey(String identityKey) {
	this.identityKey = identityKey;
    }

    public String getParentIdentityKey() {
	return parentIdentityKey;
    }

    public void setParentIdentityKey(String parentIdentityKey) {
	this.parentIdentityKey = parentIdentityKey;
    }

    public List<String> getChildIdentityKeysList() {
	return childIdentityKeysList;
    }

    public void setChildIdentityKeysList(List<String> childIdentityKeysList) {
	this.childIdentityKeysList = childIdentityKeysList;
    }

}
