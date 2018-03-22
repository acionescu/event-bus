package net.segoia.event.eventbus.peers.events.auth;

import java.util.List;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentityType;

public class NodeAuth {
    /**
     * A node may define more identities for more identity types, in the preferred order
     */
    public List<? extends NodeIdentity<? extends NodeIdentityType>> identities;

    public List<? extends NodeIdentity<? extends NodeIdentityType>> getIdentities() {
	return identities;
    }

    public void setIdentities(List<? extends NodeIdentity<? extends NodeIdentityType>> identities) {
	this.identities = identities;
    }

}
