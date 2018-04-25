package net.segoia.event.eventbus.agents;

import java.util.List;

import net.segoia.event.eventbus.peers.GlobalEventNodeAgent;
import net.segoia.event.eventbus.services.EventNodeServiceDefinition;

public class GlobalAgentRegisterRequest extends AgentRegisterRequest<GlobalEventNodeAgent> {

    public GlobalAgentRegisterRequest(GlobalEventNodeAgent agent) {
	super(agent);
    }

    public GlobalAgentRegisterRequest(GlobalEventNodeAgent agent, List<EventNodeServiceDefinition> providedServices) {
	super(agent, providedServices);
    }

}
