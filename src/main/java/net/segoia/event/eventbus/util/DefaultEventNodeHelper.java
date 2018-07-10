package net.segoia.event.eventbus.util;

import java.util.UUID;

import net.segoia.event.eventbus.peers.util.EventNodeHelper;

public class DefaultEventNodeHelper implements EventNodeHelper {
    public String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    public String generateSessionId() {
	return UUID.randomUUID().toString();
    }

    public String generateSecurityToken() {
	return UUID.randomUUID().toString();
    }

    @Override
    public String generateEventId() {
	return UUID.randomUUID().toString();
    }

    @Override
    public String[] splitString(String target, String sep) {
	return target.split(sep);
    }
}
