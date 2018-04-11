package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.CommOperationDef;
import net.segoia.event.eventbus.peers.events.session.SessionKey;

public class CommOperationContext<D extends CommOperationDef> extends OperationContext<D> {

    private SessionKey sessionKey;

    public CommOperationContext(D opDef, SessionKey sessionKey) {
	super(opDef);
	this.sessionKey = sessionKey;
    }

    public SessionKey getSessionKey() {
	return sessionKey;
    }

}
