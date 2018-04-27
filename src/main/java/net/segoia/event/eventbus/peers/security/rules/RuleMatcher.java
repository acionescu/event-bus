package net.segoia.event.eventbus.peers.security.rules;

public abstract class RuleMatcher {
    private String type;

    public RuleMatcher(String type) {
	super();
	this.type = type;
    }

    public abstract boolean matches(RuleMatchContext context);

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

}
