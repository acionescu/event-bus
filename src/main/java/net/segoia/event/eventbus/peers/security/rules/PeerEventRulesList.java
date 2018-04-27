package net.segoia.event.eventbus.peers.security.rules;

import java.util.List;

import net.segoia.event.eventbus.peers.manager.states.PeerStateContext;

public abstract class PeerEventRulesList {
    private List<PeerEventRule> rules;
    private String type;

    public PeerEventRulesList(String type) {
	super();
	this.type = type;
    }

    public PeerEventRulesList(List<PeerEventRule> rules, String type) {
	super();
	this.rules = rules;
	this.type = type;
    }

    public abstract boolean isSatisfied(PeerStateContext context);

    public boolean matchesAny(PeerStateContext context) {
	for (PeerEventRule r : rules) {
	    if (r.matches(context)) {
		return true;
	    }
	}
	return false;
    }

    public List<PeerEventRule> getRules() {
	return rules;
    }

    public void setRules(List<PeerEventRule> rules) {
	this.rules = rules;
    }

}
