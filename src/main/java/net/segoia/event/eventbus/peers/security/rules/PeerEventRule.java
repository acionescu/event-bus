package net.segoia.event.eventbus.peers.security.rules;

import net.segoia.event.eventbus.peers.manager.states.PeerStateContext;

public class PeerEventRule extends EventRule{
    public static final String separator="/";
    
    private RuleMatcher ruleMatcher;
    
    public static PeerEventRule buildRegexRule(String regex) {
	PeerEventRule r = new PeerEventRule();
	r.ruleMatcher = new RegexRuleMatcher(regex);
	return r;
    }
    
    public boolean matches(PeerStateContext context) {
	String stateId = context.getState().getId();
	String et = context.getEvent().getEt();
	
	String signature = stateId+separator+et;
	
	return matches(new RuleMatchContext(signature));
    }

    public RuleMatcher getRuleMatcher() {
        return ruleMatcher;
    }

    public void setRuleMatcher(RuleMatcher ruleMatcher) {
        this.ruleMatcher = ruleMatcher;
    }

    @Override
    public boolean matches(RuleMatchContext context) {
	return ruleMatcher.matches(context);
    }
    
    
}
