package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.EncryptCommOperationDef;

public class DecryptOperationContext extends RawDataOperationContext{
    private EncryptCommOperationDef opDef;

    public DecryptOperationContext(byte[] data, EncryptCommOperationDef opDef) {
	super(data);
	this.opDef = opDef;
    }

    public EncryptCommOperationDef getOpDef() {
        return opDef;
    }
    
    
}
