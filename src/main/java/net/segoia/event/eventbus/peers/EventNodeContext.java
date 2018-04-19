package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.events.NodeInfo;
import net.segoia.event.eventbus.peers.security.EventNodeSecurityManager;
import net.segoia.event.eventbus.peers.util.EventNodeHelper;
import net.segoia.event.eventbus.util.EBus;

/**
 * Defines an {@link EventNode}'s runtime context
 * 
 * @author adi
 *
 */
public class EventNodeContext {
    private EventNode node;
    private EventBusNodeConfig config;
    private EventNodeSecurityManager securityManager;
    private EventNodeHelper helper;

    public EventNodeContext(EventNode node, EventNodeSecurityManager securityManager) {
	super();
	this.node = node;
	this.config = node.getConfig();
	this.securityManager = securityManager;
	this.helper = config.getHelper();
    }

    public String getLocalNodeId() {
	return node.getId();
    }

    public NodeInfo getNodeInfo() {
	return node.getNodeInfo();
    }

    public String generatePeerId() {
	return helper.generatePeerId();
    }

    public String generateSessionId() {
	return helper.generateSessionId();
    }

    public String generateSecurityToken() {
	return helper.generateSecurityToken();
    }

    public EventNode getNode() {
	return node;
    }

    public EventBusNodeConfig getConfig() {
	return config;
    }

    public EventNodeSecurityManager getSecurityManager() {
	return securityManager;
    }

    public void postEvent(Event event) {
	node.postInternally(event);
    }

}
