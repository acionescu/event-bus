package net.segoia.event.eventbus.peers.security;

import net.segoia.event.eventbus.peers.events.auth.NodeAuth;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.IdentityType;

public abstract class PrivateIdentityData<N extends NodeIdentity<? extends IdentityType>> implements PrivateIdentityManager{
    /**
     * position in the {@link NodeAuth} identities list
     */
    private int index;

    private N publicNodeIdentity;

    public int getIndex() {
	return index;
    }

    public void setIndex(int index) {
	this.index = index;
    }

    public N getPublicNodeIdentity() {
	return publicNodeIdentity;
    }

    public void setPublicNodeIdentity(N publicNodeIdentity) {
	this.publicNodeIdentity = publicNodeIdentity;
    }

    @Override
    public String getType() {
	return getPublicNodeIdentity().getType().getType();
    }

}
