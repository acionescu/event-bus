package net.segoia.event.eventbus.peers.events.auth.id;

public abstract class NodeIdentity<T extends IdentityType> {
    private T type;
    /**
     * Data that certifies this particular type of identity
     */
    private NodeIdentityCertificationData certificationData;

    public NodeIdentity(T type) {
	super();
	this.type = type;
    }

    public T getType() {
	return type;
    }

    public void setType(T type) {
	this.type = type;
    }

    public NodeIdentityCertificationData getCertificationData() {
        return certificationData;
    }

    public void setCertificationData(NodeIdentityCertificationData certificationData) {
        this.certificationData = certificationData;
    }

}
