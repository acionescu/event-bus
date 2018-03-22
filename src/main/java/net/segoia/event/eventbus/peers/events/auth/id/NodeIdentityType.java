package net.segoia.event.eventbus.peers.events.auth.id;

public abstract class NodeIdentityType {
    /* type of identity */
    private String type;

    public NodeIdentityType(String type) {
	super();
	this.type = type;
    }

    public String getType() {
	return type;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((type == null) ? 0 : type.hashCode());
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
	NodeIdentityType other = (NodeIdentityType) obj;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	return true;
    }

    
}
