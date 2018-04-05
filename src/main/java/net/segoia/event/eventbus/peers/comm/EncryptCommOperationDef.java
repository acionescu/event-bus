package net.segoia.event.eventbus.peers.comm;

public class EncryptCommOperationDef extends CommOperationDef {
    public static final String TYPE = "E";

    private String transformation;

    public EncryptCommOperationDef() {
	super(TYPE);
    }

    public String getTransformation() {
	return transformation;
    }

    public void setTransformation(String transformation) {
	this.transformation = transformation;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
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
	EncryptCommOperationDef other = (EncryptCommOperationDef) obj;
	if (transformation == null) {
	    if (other.transformation != null)
		return false;
	} else if (!transformation.equals(other.transformation))
	    return false;
	return true;
    }

}
