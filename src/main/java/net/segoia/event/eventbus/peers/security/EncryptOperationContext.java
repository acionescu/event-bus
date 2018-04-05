package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.EncryptCommOperationDef;

public class EncryptOperationContext extends OperationContext {

    private EncryptCommOperationDef opDef;

    public EncryptOperationContext(byte[] data, EncryptCommOperationDef opDef) {
	super(data);
	this.opDef = opDef;
    }

    public EncryptCommOperationDef getOpDef() {
	return opDef;
    }

    public void setOpDef(EncryptCommOperationDef opDef) {
	this.opDef = opDef;
    }

}
