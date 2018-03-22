package net.segoia.event.eventbus.peers.comm;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentityType;

public class NodeCommunicationStrategy {
    /**
     * The required tx node identity type for this strategy
     */
    private NodeIdentityType txNodeIdentityType;
    
    /**
     * The required rx node identity type for this strategy
     */
    private NodeIdentityType rxNodeIdentityType;
    
    /**
     * Defines the strategy used to send data to a direct peer
     */
    private String directTxStrategy;

    /**
     * Defines the strategy used to relay events between peers
     */
    private String relayCommStrategy;

    public String getDirectTxStrategy() {
	return directTxStrategy;
    }

    public void setDirectTxStrategy(String directTxStrategy) {
	this.directTxStrategy = directTxStrategy;
    }

    public String getRelayCommStrategy() {
	return relayCommStrategy;
    }

    public void setRelayCommStrategy(String relayCommStrategy) {
	this.relayCommStrategy = relayCommStrategy;
    }

    public NodeIdentityType getTxNodeIdentityType() {
        return txNodeIdentityType;
    }

    public void setTxNodeIdentityType(NodeIdentityType txNodeIdentityType) {
        this.txNodeIdentityType = txNodeIdentityType;
    }

    public NodeIdentityType getRxNodeIdentityType() {
        return rxNodeIdentityType;
    }

    public void setRxNodeIdentityType(NodeIdentityType rxNodeIdentityType) {
        this.rxNodeIdentityType = rxNodeIdentityType;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((directTxStrategy == null) ? 0 : directTxStrategy.hashCode());
	result = prime * result + ((txNodeIdentityType == null) ? 0 : txNodeIdentityType.hashCode());
	result = prime * result + ((rxNodeIdentityType == null) ? 0 : rxNodeIdentityType.hashCode());
	result = prime * result + ((relayCommStrategy == null) ? 0 : relayCommStrategy.hashCode());
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
	NodeCommunicationStrategy other = (NodeCommunicationStrategy) obj;
	if (directTxStrategy == null) {
	    if (other.directTxStrategy != null)
		return false;
	} else if (!directTxStrategy.equals(other.directTxStrategy))
	    return false;
	if (txNodeIdentityType == null) {
	    if (other.txNodeIdentityType != null)
		return false;
	} else if (!txNodeIdentityType.equals(other.txNodeIdentityType))
	    return false;
	if (rxNodeIdentityType == null) {
	    if (other.rxNodeIdentityType != null)
		return false;
	} else if (!rxNodeIdentityType.equals(other.rxNodeIdentityType))
	    return false;
	if (relayCommStrategy == null) {
	    if (other.relayCommStrategy != null)
		return false;
	} else if (!relayCommStrategy.equals(other.relayCommStrategy))
	    return false;
	return true;
    }

   
}
