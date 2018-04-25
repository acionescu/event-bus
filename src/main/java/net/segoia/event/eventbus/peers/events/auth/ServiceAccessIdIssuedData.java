package net.segoia.event.eventbus.peers.events.auth;

import java.util.List;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
/**
 * !!!!! Warning, this can be sent over the network only over an encrypted connection
 * @author adi
 *
 */
import net.segoia.event.eventbus.services.EventNodeServiceAccessPolicy;

public class ServiceAccessIdIssuedData {
    private NodeIdentity<?> accessIdentity;
    private List<EventNodeServiceAccessPolicy> servicePolicies;

    public ServiceAccessIdIssuedData(NodeIdentity<?> accessIdentity,
	    List<EventNodeServiceAccessPolicy> servicePolicies) {
	super();
	this.accessIdentity = accessIdentity;
	this.servicePolicies = servicePolicies;
    }

    public ServiceAccessIdIssuedData() {
	super();
	// TODO Auto-generated constructor stub
    }

    public NodeIdentity<?> getAccessIdentity() {
	return accessIdentity;
    }

    public void setAccessIdentity(NodeIdentity<?> accessIdentity) {
	this.accessIdentity = accessIdentity;
    }

    public List<EventNodeServiceAccessPolicy> getServicePolicies() {
	return servicePolicies;
    }

    public void setServicePolicies(List<EventNodeServiceAccessPolicy> servicePolicies) {
	this.servicePolicies = servicePolicies;
    }

}
