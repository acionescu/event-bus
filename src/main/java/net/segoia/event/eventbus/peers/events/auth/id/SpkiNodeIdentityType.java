package net.segoia.event.eventbus.peers.events.auth.id;

public class SpkiNodeIdentityType extends IdentityType {
    public static final String TYPE = "SPKI";

    private String algorithm;

    private int keySize;

    public SpkiNodeIdentityType() {
	super(TYPE);
    }

    public SpkiNodeIdentityType(String algorithm, int keySize) {
	this();
	this.algorithm = algorithm;
	this.keySize = keySize;
    }

    public int getKeySize() {
	return keySize;
    }

    public void setKeySize(int keySize) {
	this.keySize = keySize;
    }

    public String getAlgorithm() {
	return algorithm;
    }

    public void setAlgorithm(String algorithm) {
	this.algorithm = algorithm;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((algorithm == null) ? 0 : algorithm.hashCode());
	result = prime * result + keySize;
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
	SpkiNodeIdentityType other = (SpkiNodeIdentityType) obj;
	if (algorithm == null) {
	    if (other.algorithm != null)
		return false;
	} else if (!algorithm.equals(other.algorithm))
	    return false;
	if (keySize != other.keySize)
	    return false;
	return true;
    }

}
