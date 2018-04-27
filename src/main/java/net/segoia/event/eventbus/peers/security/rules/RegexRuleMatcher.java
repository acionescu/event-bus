package net.segoia.event.eventbus.peers.security.rules;

import java.util.regex.Pattern;

public class RegexRuleMatcher extends RuleMatcher {
    public static final String TYPE = "REGEX";
    private String regex;
    private Pattern pattern;

    public RegexRuleMatcher() {
	super(TYPE);
    }

    public RegexRuleMatcher( String regex) {
	this();
	this.regex = regex;
    }

    protected Pattern getPattern() {
	if (pattern == null) {
	    pattern = Pattern.compile(regex);
	}
	return pattern;
    }

    public String getRegex() {
	return regex;
    }

    public void setRegex(String regex) {
	this.regex = regex;
    }

    @Override
    public boolean matches(RuleMatchContext context) {
	return getPattern().matcher(context.getEventSignature()).matches();
    }

}
