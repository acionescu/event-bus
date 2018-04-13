package net.segoia.event.eventbus.peers.comm;

import net.segoia.event.eventbus.peers.ChainedEventTransceiver;
import net.segoia.event.eventbus.peers.EventTransceiver;
import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.security.CommDataContext;
import net.segoia.event.eventbus.peers.security.CommOperationException;

public class CommProtocolEventTransceiver extends ChainedEventTransceiver {
    private PeerContext peerContext;

    private PeerCommManager peerCommManager;

    public CommProtocolEventTransceiver(EventTransceiver downstream, PeerContext peerContext) {
	super(downstream);
	this.peerContext = peerContext;
	init();
    }

    @Override
    protected void init() {
	super.init();

	peerCommManager = peerContext.getPeerCommManager();

    }

    @Override
    public void onPeerData(byte[] data) {
	CommDataContext processedData;
	try {
	    processedData = peerCommManager.processIncomingData(new CommDataContext(data));
	    receiveData(processedData.getData());
	} catch (CommOperationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    public void sendData(byte[] data) {
	try {
	    CommDataContext processedData = peerCommManager.processsOutgoingData(new CommDataContext(data));
	    super.sendData(processedData.getData());
	} catch (CommOperationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    public void start() {
	// TODO Auto-generated method stub

    }

}
