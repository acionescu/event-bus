package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.Event;

public abstract class AbstractEventTransceiver implements EventTransceiver {

    private PeerEventListener remoteEventListener;

    public PeerEventListener getRemoteEventListener() {
	return remoteEventListener;
    }

    public void setRemoteEventListener(PeerEventListener remoteEventListener) {
	this.remoteEventListener = remoteEventListener;
    }

    @Override
    public void receiveEvent(Event event) {
	remoteEventListener.onPeerEvent(event);
    }

    @Override
    public void onPeerLeaving() {
	remoteEventListener.onPeerLeaving();
    }

}
