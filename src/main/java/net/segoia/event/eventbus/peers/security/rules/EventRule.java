package net.segoia.event.eventbus.peers.security.rules;

public abstract class EventRule {
    public abstract boolean matches(RuleMatchContext context);
}
