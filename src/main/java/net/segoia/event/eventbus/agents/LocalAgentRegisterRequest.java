package net.segoia.event.eventbus.agents;

import java.util.List;

import net.segoia.event.eventbus.peers.LocalEventNodeAgent;
import net.segoia.event.eventbus.services.EventNodeServiceDefinition;

public class LocalAgentRegisterRequest extends AgentRegisterRequest<LocalEventNodeAgent>{

    public LocalAgentRegisterRequest(LocalEventNodeAgent agent, List<EventNodeServiceDefinition> providedServices) {
	super(agent, providedServices);
    }

    public LocalAgentRegisterRequest(LocalEventNodeAgent agent) {
	super(agent);
    }

}
