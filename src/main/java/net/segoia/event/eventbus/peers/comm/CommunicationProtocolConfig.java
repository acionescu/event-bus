package net.segoia.event.eventbus.peers.comm;

public class CommunicationProtocolConfig {
    /**
     * Position in the identities list of the selected server identity to be used
     */
    private int serverNodeIdentity;
    /**
     * Position in the identities list of the selected client identity to be used
     */
    private int clientNodeIdentity;

    public CommunicationProtocolConfig() {
	super();
	// TODO Auto-generated constructor stub
    }

    public CommunicationProtocolConfig(int serverNodeIdentity, int clientNodeIdentity) {
	super();
	this.serverNodeIdentity = serverNodeIdentity;
	this.clientNodeIdentity = clientNodeIdentity;
    }

    public int getServerNodeIdentity() {
	return serverNodeIdentity;
    }

    public void setServerNodeIdentity(int serverNodeIdentity) {
	this.serverNodeIdentity = serverNodeIdentity;
    }

    public int getClientNodeIdentity() {
	return clientNodeIdentity;
    }

    public void setClientNodeIdentity(int clientNodeIdentity) {
	this.clientNodeIdentity = clientNodeIdentity;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + clientNodeIdentity;
	result = prime * result + serverNodeIdentity;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	CommunicationProtocolConfig other = (CommunicationProtocolConfig) obj;
	if (clientNodeIdentity != other.clientNodeIdentity)
	    return false;
	if (serverNodeIdentity != other.serverNodeIdentity)
	    return false;
	return true;
    }

}
