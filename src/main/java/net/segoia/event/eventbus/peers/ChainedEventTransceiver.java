package net.segoia.event.eventbus.peers;

public abstract class ChainedEventTransceiver extends AbstractEventTransceiver implements PeerDataListener {
    private EventTransceiver downstream;

    public ChainedEventTransceiver(EventTransceiver downstream) {
	super();
	this.downstream = downstream;
    }

    @Override
    protected void init() {
	downstream.setRemoteDataListener(this);
    }

    @Override
    public void sendData(byte[] data) {
	downstream.sendData(data);

    }

    @Override
    public String getChannel() {
	return downstream.getChannel();
    }

    @Override
    public void terminate() {
	downstream.terminate();
    }

}
