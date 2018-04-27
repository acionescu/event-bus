package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.manager.states.PeerStateContext;
import net.segoia.event.eventbus.peers.security.rules.PeerEventRulesList;

public class IdentityRole {
    public static final String PEER_AUTH = "PEER_AUTH";
    public static final String SERVICE_ACCESS = "SERVICE_ACCESS";

    private String type;

    private PeerEventRulesList peerEventRules;

    public IdentityRole(String type, PeerEventRulesList peerEventRules) {
	super();
	this.type = type;
	this.peerEventRules = peerEventRules;
    }

    public IdentityRole() {
	super();
	// TODO Auto-generated constructor stub
    }

    public boolean isEventAccepted(PeerStateContext context) {
	if (peerEventRules == null) {
	    return true;
	}
	return peerEventRules.isSatisfied(context);
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public PeerEventRulesList getPeerEventRules() {
	return peerEventRules;
    }

    public void setPeerEventRules(PeerEventRulesList peerEventRules) {
	this.peerEventRules = peerEventRules;
    }

}
