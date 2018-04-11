package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.comm.CommOperationDef;

public class SpkiSpkiCommOperationContext<D extends CommOperationDef>
	extends SpkiCommOperationContext<D, SpkiPrivateIdentityData, SpkiPublicIdentityManager> {
    
   
    private SignOperationWorker signWorker;

    public SpkiSpkiCommOperationContext(D opDef, SpkiPrivateIdentityData ourIdentity,
	    SpkiPublicIdentityManager peerIdentity) {
	super(opDef, ourIdentity, peerIdentity);
    }

    

}
