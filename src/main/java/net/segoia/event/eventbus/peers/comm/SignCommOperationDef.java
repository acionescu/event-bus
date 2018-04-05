package net.segoia.event.eventbus.peers.comm;

public class SignCommOperationDef extends CommOperationDef {

    public static final String TYPE = "S";

    private String hashingAlgorithm;

    public SignCommOperationDef() {
	super(TYPE);
    }

    public String getHashingAlgorithm() {
	return hashingAlgorithm;
    }

    public void setHashingAlgorithm(String hashingAlgorithm) {
	this.hashingAlgorithm = hashingAlgorithm;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((hashingAlgorithm == null) ? 0 : hashingAlgorithm.hashCode());
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
	SignCommOperationDef other = (SignCommOperationDef) obj;
	if (hashingAlgorithm == null) {
	    if (other.hashingAlgorithm != null)
		return false;
	} else if (!hashingAlgorithm.equals(other.hashingAlgorithm))
	    return false;
	return true;
    }

    
}
