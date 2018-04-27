package net.segoia.event.eventbus.services;

import java.util.ArrayList;
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

    private List<String> roles;
    
    private long lastAuthTs;

    public NodeIdentityProfile(NodeIdentity<?> identity) {
	super();
	this.identity = identity;
    }

    public NodeIdentityProfile(String identityKey, NodeIdentity<?> identity) {
	super();
	this.identityKey = identityKey;
	this.identity = identity;
    }

    public NodeIdentityProfile() {
	super();
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

    public void addChildIdentityKey(String childIdKey) {
	if (childIdentityKeysList == null) {
	    childIdentityKeysList = new ArrayList<>();
	}
	childIdentityKeysList.add(childIdKey);
    }

    public boolean areServicesAccessible(List<EventNodeServiceRef> servicesRefs) {
	for (EventNodeServiceRef sr : servicesRefs) {
	    if (!serviceContracts.containsKey(sr.toString())) {
		return false;
	    }
	}
	return true;
    }

    public List<String> getRoles() {
	return roles;
    }

    public void setRoles(List<String> roles) {
	this.roles = roles;
    }

    public void addRole(String roleId) {
	if (roles == null) {
	    roles = new ArrayList<>();
	}

	if (!roles.contains(roleId)) {
	    roles.add(roleId);
	}
    }

    public long getLastAuthTs() {
        return lastAuthTs;
    }

    public void setLastAuthTs(long lastAuthTs) {
        this.lastAuthTs = lastAuthTs;
    }

}
