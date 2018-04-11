package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.EncryptCommOperationDef;

public class EncryptOperationContext extends RawDataOperationContext<EncryptCommOperationDef> {

    public EncryptOperationContext(byte[] data, EncryptCommOperationDef opDef) {
	super(opDef, data);
    }

}
