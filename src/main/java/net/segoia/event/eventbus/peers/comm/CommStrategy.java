package net.segoia.event.eventbus.peers.comm;

import java.util.List;

public class CommStrategy {
    private List<CommOperationDef> operations;

    public List<CommOperationDef> getOperations() {
	return operations;
    }

    public void setOperations(List<CommOperationDef> operations) {
	this.operations = operations;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((operations == null) ? 0 : operations.hashCode());
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
	CommStrategy other = (CommStrategy) obj;
	if (operations == null) {
	    if (other.operations != null)
		return false;
	} else if (!operations.equals(other.operations))
	    return false;
	return true;
    }

}
