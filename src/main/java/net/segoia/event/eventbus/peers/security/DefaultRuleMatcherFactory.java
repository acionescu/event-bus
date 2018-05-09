package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.security.rules.RegexRuleMatcher;
import net.segoia.event.eventbus.peers.security.rules.RuleMatcher;
import net.segoia.event.eventbus.peers.security.rules.RuleMatcherFactory;

public class DefaultRuleMatcherFactory implements RuleMatcherFactory{

    @Override
    public RuleMatcher buildRegexRuleMatcher(String regex) {
	return new RegexRuleMatcher(regex);
    }

}
