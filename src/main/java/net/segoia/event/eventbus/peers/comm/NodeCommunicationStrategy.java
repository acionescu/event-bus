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
    private CommStrategy directTxStrategy;

    /**
     * Defines the strategy used to relay events between peers
     */
    private CommStrategy relayCommStrategy;
    
    /**
     * The strategy used to transmit session key
     */
    private CommStrategy sessionTxStrategy;
    

    public CommStrategy getDirectTxStrategy() {
	return directTxStrategy;
    }

    public void setDirectTxStrategy(CommStrategy directTxStrategy) {
	this.directTxStrategy = directTxStrategy;
    }

    public CommStrategy getRelayCommStrategy() {
	return relayCommStrategy;
    }

    public void setRelayCommStrategy(CommStrategy relayCommStrategy) {
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

    public CommStrategy getSessionTxStrategy() {
        return sessionTxStrategy;
    }

    public void setSessionTxStrategy(CommStrategy sessionTxStrategy) {
        this.sessionTxStrategy = sessionTxStrategy;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((directTxStrategy == null) ? 0 : directTxStrategy.hashCode());
	result = prime * result + ((relayCommStrategy == null) ? 0 : relayCommStrategy.hashCode());
	result = prime * result + ((rxNodeIdentityType == null) ? 0 : rxNodeIdentityType.hashCode());
	result = prime * result + ((sessionTxStrategy == null) ? 0 : sessionTxStrategy.hashCode());
	result = prime * result + ((txNodeIdentityType == null) ? 0 : txNodeIdentityType.hashCode());
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
	if (relayCommStrategy == null) {
	    if (other.relayCommStrategy != null)
		return false;
	} else if (!relayCommStrategy.equals(other.relayCommStrategy))
	    return false;
	if (rxNodeIdentityType == null) {
	    if (other.rxNodeIdentityType != null)
		return false;
	} else if (!rxNodeIdentityType.equals(other.rxNodeIdentityType))
	    return false;
	if (sessionTxStrategy == null) {
	    if (other.sessionTxStrategy != null)
		return false;
	} else if (!sessionTxStrategy.equals(other.sessionTxStrategy))
	    return false;
	if (txNodeIdentityType == null) {
	    if (other.txNodeIdentityType != null)
		return false;
	} else if (!txNodeIdentityType.equals(other.txNodeIdentityType))
	    return false;
	return true;
    }

 

}
