package net.segoia.event.eventbus.peers.events.auth;

import java.util.List;

import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.IdentityType;

public class NodeAuth {
    /**
     * A node may define more identities for more identity types, in the preferred order
     */
    public List<? extends NodeIdentity<? extends IdentityType>> identities;

    public List<? extends NodeIdentity<? extends IdentityType>> getIdentities() {
	return identities;
    }

    public void setIdentities(List<? extends NodeIdentity<? extends IdentityType>> identities) {
	this.identities = identities;
    }

}
