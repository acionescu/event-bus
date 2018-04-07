package net.segoia.event.eventbus.peers;

public abstract class AbstractEventTransceiver implements EventTransceiver {

    private PeerDataListener remoteDataListener;
    
    protected abstract void init();

    public PeerDataListener getRemoteDataListener() {
	return remoteDataListener;
    }

    public void setRemoteDataListener(PeerDataListener remoteDataListener) {
	this.remoteDataListener = remoteDataListener;
    }

    @Override
    public void receiveData(byte[] data) {
	remoteDataListener.onPeerData(data);
    }

    @Override
    public void onPeerLeaving() {
	remoteDataListener.onPeerLeaving();

    }

    // @Override
    // public void receiveEvent(Event event) {
    // remoteEventListener.onPeerEvent(event);
    // }
    //
    // @Override
    // public void onPeerLeaving() {
    // remoteEventListener.onPeerLeaving();
    // }

}
