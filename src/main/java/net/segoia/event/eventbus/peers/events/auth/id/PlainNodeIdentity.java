package net.segoia.event.eventbus.peers.events.auth.id;

public class PlainNodeIdentity extends NodeIdentity<PlainNodeIdentityType> {

    public PlainNodeIdentity() {
	super(new PlainNodeIdentityType());
    }
}
