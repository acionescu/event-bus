package net.segoia.event.eventbus.peers.comm;

public class CommunicationProtocolDefinition {
    private NodeCommunicationStrategy serverCommStrategy;
    private NodeCommunicationStrategy clientCommStrategy;

    public CommunicationProtocolDefinition() {
	super();
    }

    public CommunicationProtocolDefinition(NodeCommunicationStrategy serverCommStrategy,
	    NodeCommunicationStrategy clientCommStrategy) {
	super();
	this.serverCommStrategy = serverCommStrategy;
	this.clientCommStrategy = clientCommStrategy;
    }

    public NodeCommunicationStrategy getServerCommStrategy() {
	return serverCommStrategy;
    }

    public void setServerCommStrategy(NodeCommunicationStrategy serverCommStrategy) {
	this.serverCommStrategy = serverCommStrategy;
    }

    public NodeCommunicationStrategy getClientCommStrategy() {
	return clientCommStrategy;
    }

    public void setClientCommStrategy(NodeCommunicationStrategy clientCommStrategy) {
	this.clientCommStrategy = clientCommStrategy;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((clientCommStrategy == null) ? 0 : clientCommStrategy.hashCode());
	result = prime * result + ((serverCommStrategy == null) ? 0 : serverCommStrategy.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	CommunicationProtocolDefinition other = (CommunicationProtocolDefinition) obj;
	if (clientCommStrategy == null) {
	    if (other.clientCommStrategy != null)
		return false;
	} else if (!clientCommStrategy.equals(other.clientCommStrategy))
	    return false;
	if (serverCommStrategy == null) {
	    if (other.serverCommStrategy != null)
		return false;
	} else if (!serverCommStrategy.equals(other.serverCommStrategy))
	    return false;
	return true;
    }

}
