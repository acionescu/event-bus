package net.segoia.event.eventbus.peers.security;

public class CombinedCommManagerBuilder implements CommManagerBuilder<CombinedCommProtocolContext> {
    private CommManagerConfig config;

    public CombinedCommManagerBuilder(CommManagerConfig config) {
	super();
	this.config = config;
    }

    public CombinedCommManagerBuilder() {
	super();
    }

    @Override
    public CommManager build(CombinedCommProtocolContext context) {
	return new CombinedCommManager(context, config);
    }

}
