package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.SignCommOperationDef;

public class SignCommOperationContext extends RawDataOperationContext {
    private SignCommOperationDef opDef;

    public SignCommOperationContext(byte[] data, SignCommOperationDef opDef) {
	super(data);
	this.opDef = opDef;
    }

    public SignCommOperationDef getOpDef() {
	return opDef;
    }

}
