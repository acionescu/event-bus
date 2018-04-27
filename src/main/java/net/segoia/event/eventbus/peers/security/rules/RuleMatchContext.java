package net.segoia.event.eventbus.peers.security.rules;

public class RuleMatchContext {
    private String eventSignature;

    public RuleMatchContext(String eventSignature) {
	super();
	this.eventSignature = eventSignature;
    }

    public String getEventSignature() {
	return eventSignature;
    }

    public void setEventSignature(String eventSignature) {
	this.eventSignature = eventSignature;
    }

}
