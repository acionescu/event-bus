package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.CommOperationDef;

public class OperationContext<D extends OperationDef> {
    private D opDef;

    public OperationContext(D opDef) {
	super();
	this.opDef = opDef;
    }

    public D getOpDef() {
	return opDef;
    }

    public void setOpDef(D opDef) {
	this.opDef = opDef;
    }

}
