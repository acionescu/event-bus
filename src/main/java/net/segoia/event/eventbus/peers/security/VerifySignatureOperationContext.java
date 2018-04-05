package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;

public class VerifySignatureOperationContext extends OperationContext {
    private String signature;
    private SignCommOperationDef opDef;

    public VerifySignatureOperationContext(byte[] data, String signature, SignCommOperationDef opDef) {
	super(data);
	this.signature = signature;
	this.opDef = opDef;
    }

    public String getSignature() {
	return signature;
    }

    public SignCommOperationDef getOpDef() {
	return opDef;
    }

}
