package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.EncryptWithPublicCommOperationDef;
import net.segoia.event.eventbus.peers.vo.comm.SignCommOperationDef;

public class SpkiSpkiCommManagerBuilder implements CommManagerBuilder<SpkiCommProtocolContext> {
    private CommManagerConfig config;

    public SpkiSpkiCommManagerBuilder() {
	super();
    }

    public SpkiSpkiCommManagerBuilder(CommManagerConfig config) {
	super();
	this.config = config;
    }

    @Override
    public SpkiSpkiCommManager build(SpkiCommProtocolContext context) {
	SpkiSpkiCommManager commManager = new SpkiSpkiCommManager(context,config);
	

	return commManager;
    }
}
