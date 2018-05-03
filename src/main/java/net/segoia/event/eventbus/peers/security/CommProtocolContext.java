package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.vo.comm.CommStrategy;

public class CommProtocolContext {

    private CommStrategy txStrategy;
    private CommStrategy rxStrategy;

    public CommProtocolContext(CommStrategy txStrategy, CommStrategy rxStrategy) {
	super();
	this.txStrategy = txStrategy;
	this.rxStrategy = rxStrategy;
    }

    public CommStrategy getTxStrategy() {
	return txStrategy;
    }

    public CommStrategy getRxStrategy() {
	return rxStrategy;
    }

}
