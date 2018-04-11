package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;

public class VerifySignatureOperationContext extends OperationContext<SignCommOperationDef> {
    private SignCommOperationOutput signOutput;

    public VerifySignatureOperationContext(SignCommOperationDef opDef, SignCommOperationOutput signOutput) {
	super(opDef);
	this.signOutput = signOutput;
    }

    public SignCommOperationOutput getSignOutput() {
	return signOutput;
    }

}
