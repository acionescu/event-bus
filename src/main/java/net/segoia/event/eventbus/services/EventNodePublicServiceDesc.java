package net.segoia.event.eventbus.services;

public class EventNodePublicServiceDesc {
    private String serviceId;
    private String serviceName;
    private String serviceDesc;
    private int version;

    public EventNodePublicServiceDesc(String serviceId, String serviceName, String serviceDesc, int version) {
	super();
	this.serviceId = serviceId;
	this.serviceName = serviceName;
	this.serviceDesc = serviceDesc;
	this.version = version;
    }

    public EventNodePublicServiceDesc(String serviceId, String serviceName, String serviceDesc) {
	super();
	this.serviceId = serviceId;
	this.serviceName = serviceName;
	this.serviceDesc = serviceDesc;
    }

    public EventNodePublicServiceDesc() {
	super();
    }

    public String getServiceId() {
	return serviceId;
    }

    public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
    }

    public String getServiceName() {
	return serviceName;
    }

    public void setServiceName(String serviceName) {
	this.serviceName = serviceName;
    }

    public String getServiceDesc() {
	return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
	this.serviceDesc = serviceDesc;
    }

    public int getVersion() {
	return version;
    }

    public void setVersion(int version) {
	this.version = version;
    }

}
