package net.segoia.event.eventbus.peers.events.auth;

import java.util.List;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
/**
 * !!!!! Warning, this can be sent over the network only over an encrypted connection
 * @author adi
 *
 */
import net.segoia.event.eventbus.services.EventNodeServiceAccessPolicy;
import net.segoia.event.eventbus.services.ServiceContract;

public class ServiceAccessIdIssuedData {
    private NodeIdentity<?> accessIdentity;
    private List<ServiceContract> servicePolicies;

    public ServiceAccessIdIssuedData(NodeIdentity<?> accessIdentity, List<ServiceContract> servicePolicies) {
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

    public List<ServiceContract> getServicePolicies() {
	return servicePolicies;
    }

    public void setServicePolicies(List<ServiceContract> servicePolicies) {
	this.servicePolicies = servicePolicies;
    }

}
