package net.segoia.event.eventbus.peers.events.auth.id;

public class SpkiNodeIdentityType extends NodeIdentityType {
    /**
     * The transformation used to generate the key
     */
    private String transformation;
    /**
     * The algorithm used to sign
     */
    private String signatureAlgorithm;

    private int keySize;

    public SpkiNodeIdentityType() {
	super("SPKI");
    }

    protected SpkiNodeIdentityType(String type) {
	super(type);
    }

    public String getTransformation() {
	return transformation;
    }

    public void setTransformation(String transformation) {
	this.transformation = transformation;
    }

    public String getSignatureAlgorithm() {
	return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
	this.signatureAlgorithm = signatureAlgorithm;
    }

    public int getKeySize() {
	return keySize;
    }

    public void setKeySize(int keySize) {
	this.keySize = keySize;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + keySize;
	result = prime * result + ((signatureAlgorithm == null) ? 0 : signatureAlgorithm.hashCode());
	result = prime * result + ((transformation == null) ? 0 : transformation.hashCode());
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
	if (keySize != other.keySize)
	    return false;
	if (signatureAlgorithm == null) {
	    if (other.signatureAlgorithm != null)
		return false;
	} else if (!signatureAlgorithm.equals(other.signatureAlgorithm))
	    return false;
	if (transformation == null) {
	    if (other.transformation != null)
		return false;
	} else if (!transformation.equals(other.transformation))
	    return false;
	return true;
    }

}
