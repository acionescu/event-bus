package net.segoia.event.eventbus.peers.security.rules;

import net.segoia.event.eventbus.peers.manager.states.PeerStateContext;

public class PeerEventWhiteList extends PeerEventRulesList{
    public static final String TYPE="WHITE_LIST";

    public PeerEventWhiteList() {
	super(TYPE);
    }

    @Override
    public boolean isSatisfied(PeerStateContext context) {
	return matchesAny(context);
    }
    
    
}
