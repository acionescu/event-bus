package net.segoia.event.eventbus.services;

public class EventNodeServiceRef {
    private String serviceId;
    private int serviceVersion;

    public EventNodeServiceRef() {
	super();
    }

    public EventNodeServiceRef(String serviceId, int serviceVersion) {
	super();
	this.serviceId = serviceId;
	this.serviceVersion = serviceVersion;
    }

    public String getServiceId() {
	return serviceId;
    }

    public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
    }

    public int getServiceVersion() {
	return serviceVersion;
    }

    public void setServiceVersion(int serviceVersion) {
	this.serviceVersion = serviceVersion;
    }
    
    

    @Override
    public String toString() {
	return serviceId+"_"+serviceVersion;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
	result = prime * result + serviceVersion;
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
	EventNodeServiceRef other = (EventNodeServiceRef) obj;
	if (serviceId == null) {
	    if (other.serviceId != null)
		return false;
	} else if (!serviceId.equals(other.serviceId))
	    return false;
	if (serviceVersion != other.serviceVersion)
	    return false;
	return true;
    }

}
