package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;

public class VerifySignatureOperationContext extends OperationContext {
    private SignCommOperationDef opDef;
    private SignCommOperationOutput signOutput;

    public VerifySignatureOperationContext(SignCommOperationDef opDef, SignCommOperationOutput signOutput) {
	super();
	this.opDef = opDef;
	this.signOutput = signOutput;
    }

    public SignCommOperationDef getOpDef() {
	return opDef;
    }

    public SignCommOperationOutput getSignOutput() {
        return signOutput;
    }

}
