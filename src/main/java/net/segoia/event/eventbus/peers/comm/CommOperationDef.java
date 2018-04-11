package net.segoia.event.eventbus.peers.comm;

import net.segoia.event.eventbus.peers.security.OperationDef;

public class CommOperationDef extends OperationDef{
    private String type;

    public CommOperationDef(String type) {
	super();
	this.type = type;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
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
	CommOperationDef other = (CommOperationDef) obj;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	return true;
    }

}
