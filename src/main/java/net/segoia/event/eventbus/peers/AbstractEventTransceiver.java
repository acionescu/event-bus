package net.segoia.event.eventbus.peers;

import net.segoia.event.eventbus.peers.events.PeerData;
import net.segoia.event.eventbus.peers.events.PeerDataEvent;

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
	receiveData(new PeerDataEvent(new PeerData(data)));
    }

    @Override
    public void receiveData(PeerDataEvent dataEvent) {
	remoteDataListener.onPeerData(dataEvent);
    }

    @Override
    public void onPeerLeaving(PeerLeavingReason reason) {
	remoteDataListener.onPeerLeaving(reason);

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
