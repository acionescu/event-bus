package net.segoia.event.eventbus.agents;

import java.util.ArrayList;
import java.util.List;

import net.segoia.event.eventbus.peers.EventNodeAgent;
import net.segoia.event.eventbus.services.EventNodeServiceDefinition;

public class AgentRegisterRequest<A extends EventNodeAgent> {
    private A agent;

    /**
     * True if this agent provides public services
     */
    private boolean publicServiceProvider;

    private List<EventNodeServiceDefinition> providedServices;

    public AgentRegisterRequest(A agent, List<EventNodeServiceDefinition> providedServices) {
	super();
	this.agent = agent;
	this.providedServices = providedServices;
    }

    public AgentRegisterRequest(A agent) {
	super();
	this.agent = agent;
    }

    public A getAgent() {
	return agent;
    }

    public void setAgent(A agent) {
	this.agent = agent;
    }

    public void setProvidedServices(List<EventNodeServiceDefinition> providedServices) {
	this.providedServices = providedServices;
    }

    public void addService(EventNodeServiceDefinition serviceDef) {
	if (providedServices == null) {
	    providedServices = new ArrayList<>();
	}

	providedServices.add(serviceDef);
    }

    public boolean isPublicServiceProvider() {
	return publicServiceProvider;
    }

    public void setPublicServiceProvider(boolean publicServiceProvider) {
	this.publicServiceProvider = publicServiceProvider;
    }

    public List<EventNodeServiceDefinition> getProvidedServices() {
	return providedServices;
    }

}
