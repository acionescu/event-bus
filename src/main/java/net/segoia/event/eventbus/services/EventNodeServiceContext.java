package net.segoia.event.eventbus.services;

import net.segoia.event.eventbus.peers.EventNodeAgent;

public class EventNodeServiceContext {
    private EventNodeAgent provider;
    private EventNodeServiceDefinition serviceDef;

    public EventNodeServiceContext(EventNodeAgent provider, EventNodeServiceDefinition serviceDef) {
	super();
	this.provider = provider;
	this.serviceDef = serviceDef;
    }

    public EventNodeServiceContext() {
	super();
	// TODO Auto-generated constructor stub
    }

    public EventNodeAgent getProvider() {
	return provider;
    }

    public void setProvider(EventNodeAgent provider) {
	this.provider = provider;
    }

    public EventNodeServiceDefinition getServiceDef() {
	return serviceDef;
    }

    public void setServiceDef(EventNodeServiceDefinition serviceDef) {
	this.serviceDef = serviceDef;
    }

}
