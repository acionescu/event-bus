package net.segoia.event.eventbus.peers.security;

public class SignCommOperationOutput extends CommOperationOutput {

    public SignCommOperationOutput(byte[] data, byte[] signature) {
	super(data);
	this.signature = signature;
    }

    private byte[] signature;

    public byte[] getSignature() {
	return signature;
    }

}
