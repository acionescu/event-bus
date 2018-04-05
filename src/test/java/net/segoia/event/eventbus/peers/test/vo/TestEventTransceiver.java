package net.segoia.event.eventbus.peers.test.vo;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.AbstractEventTransceiver;
import net.segoia.event.eventbus.util.EBus;

public abstract class TestEventTransceiver extends AbstractEventTransceiver {
    /**
     * keep the pair transceiver instance
     */
    private transient TestEventTransceiver pairTransceiver;

    private boolean sendAsync;

    @Override
    public void sendEvent(Event event) {
	EBus.postEvent(event);
	if (!sendAsync) {
	    pairTransceiver.receiveEvent(event);
	} else {
	    new Thread() {

		@Override
		public void run() {
		    pairTransceiver.receiveEvent(event);
		}

	    }.start();
	}
    }

    @Override
    public void terminate() {
	pairTransceiver.onPeerLeaving();
    }

    @Override
    public String getChannel() {
	return "TEST_LOCAL";
    }

    public TestEventTransceiver getPairTransceiver() {
	return pairTransceiver;
    }

    public void setPairTransceiver(TestEventTransceiver pairTransceiver) {
	this.pairTransceiver = pairTransceiver;
    }

    public boolean isSendAsync() {
        return sendAsync;
    }

    public void setSendAsync(boolean sendAsync) {
        this.sendAsync = sendAsync;
    }

}
