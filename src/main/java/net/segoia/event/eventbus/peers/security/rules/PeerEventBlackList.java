package net.segoia.event.eventbus.peers.security.rules;

import java.util.List;

import net.segoia.event.eventbus.peers.manager.states.PeerStateContext;

public class PeerEventBlackList extends PeerEventRulesList {
    public static final String TYPE = "BLACK_LIST";

    public PeerEventBlackList() {
	super(TYPE);
    }

    public PeerEventBlackList(List<PeerEventRule> rules) {
	super(rules, TYPE);
    }

    @Override
    public boolean isSatisfied(PeerStateContext context) {
	return !matchesAny(context);
    }

}
