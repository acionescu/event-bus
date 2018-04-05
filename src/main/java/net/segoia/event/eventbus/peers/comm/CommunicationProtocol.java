package net.segoia.event.eventbus.peers.comm;

/**
 * Defines the communication protocol between two nodes
 * 
 * @author adi
 *
 */
public class CommunicationProtocol {
    private CommunicationProtocolDefinition protocolDefinition;
    private CommunicationProtocolConfig config;

    public CommunicationProtocol() {
	super();
    }

    public CommunicationProtocol(CommunicationProtocolDefinition protocolDefinition,
	    CommunicationProtocolConfig config) {
	super();
	this.protocolDefinition = protocolDefinition;
	this.config = config;
    }

    public CommunicationProtocolDefinition getProtocolDefinition() {
	return protocolDefinition;
    }

    public void setProtocolDefinition(CommunicationProtocolDefinition protocolDefinition) {
	this.protocolDefinition = protocolDefinition;
    }

    public CommunicationProtocolConfig getConfig() {
	return config;
    }

    public void setConfig(CommunicationProtocolConfig config) {
	this.config = config;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((config == null) ? 0 : config.hashCode());
	result = prime * result + ((protocolDefinition == null) ? 0 : protocolDefinition.hashCode());
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
	CommunicationProtocol other = (CommunicationProtocol) obj;
	if (config == null) {
	    if (other.config != null)
		return false;
	} else if (!config.equals(other.config))
	    return false;
	if (protocolDefinition == null) {
	    if (other.protocolDefinition != null)
		return false;
	} else if (!protocolDefinition.equals(other.protocolDefinition))
	    return false;
	return true;
    }

}
