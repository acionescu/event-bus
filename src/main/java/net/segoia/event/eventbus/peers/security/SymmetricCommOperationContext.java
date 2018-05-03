package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.CommOperationDef;

public class SymmetricCommOperationContext<D extends CommOperationDef> extends OperationContext<D> {

    private SharedSecretIdentityManager identityManager;

    public SymmetricCommOperationContext(D opDef, SharedSecretIdentityManager identityManager) {
	super(opDef);
	this.identityManager = identityManager;
    }

    public SharedSecretIdentityManager getIdentityManager() {
        return identityManager;
    }
}
