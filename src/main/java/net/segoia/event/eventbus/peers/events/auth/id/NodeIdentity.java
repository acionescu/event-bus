package net.segoia.event.eventbus.peers.events.auth.id;

public abstract class NodeIdentity<T extends NodeIdentityType> {
    private T type;

    public NodeIdentity() {
	super();
    }

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

}
