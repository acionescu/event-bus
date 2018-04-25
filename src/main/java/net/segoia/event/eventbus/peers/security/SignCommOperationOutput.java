package net.segoia.event.eventbus.peers.security;

public class SignCommOperationOutput extends CommOperationOutput {
    private byte[] signature;

    public SignCommOperationOutput(byte[] data, byte[] signature) {
	super(data);
	this.signature = signature;
    }

    public byte[] getSignature() {
	return signature;
    }

}
