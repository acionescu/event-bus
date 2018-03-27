package net.segoia.event.eventbus.peers.util;

import java.util.UUID;

public class EventNodeHelper {

    public String generatePeerId() {
	return UUID.randomUUID().toString();
    }

    public String generateSessionId() {
	return UUID.randomUUID().toString();
    }

    public String generateSecurityToken() {
	return UUID.randomUUID().toString();
    }

}
