package net.segoia.event.eventbus.peers;

public abstract class AbstractEventTransceiver implements EventTransceiver {

    private PeerEventListener remoteEventListener;

    public PeerEventListener getRemoteEventListener() {
	return remoteEventListener;
    }

    public void setRemoteEventListener(PeerEventListener remoteEventListener) {
	this.remoteEventListener = remoteEventListener;
    }

}
