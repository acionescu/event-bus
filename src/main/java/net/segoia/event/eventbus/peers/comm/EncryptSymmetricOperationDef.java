package net.segoia.event.eventbus.peers.comm;

public class EncryptSymmetricOperationDef extends CommOperationDef {
    public static final String TYPE = "ES";

    private String transformation;

    public EncryptSymmetricOperationDef() {
	super(TYPE);
    }

    public String getTransformation() {
	return transformation;
    }

    public void setTransformation(String transformation) {
	this.transformation = transformation;
    }

}
