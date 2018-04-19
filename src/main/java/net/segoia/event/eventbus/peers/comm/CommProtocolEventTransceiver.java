package net.segoia.event.eventbus.peers.comm;

import net.segoia.event.eventbus.peers.ChainedEventTransceiver;
import net.segoia.event.eventbus.peers.EventTransceiver;
import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.events.PeerDataEvent;
import net.segoia.event.eventbus.peers.security.CommDataContext;
import net.segoia.event.eventbus.peers.security.CommOperationException;
import net.segoia.util.crypto.CryptoUtil;

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
    public void onPeerData(PeerDataEvent dataEvent) {
	CommDataContext processedData;
	byte[] data = dataEvent.getData().getData();
	try {
	    byte[] decodedData = CryptoUtil.base64DecodeToBytes(data);
	    processedData = peerCommManager.processIncomingData(new CommDataContext(decodedData));
	    dataEvent.getData().setData(processedData.getData());
	    receiveData(dataEvent);
	} catch (CommOperationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    public void sendData(byte[] data) {
	try {
	    CommDataContext processedData = peerCommManager.processsOutgoingData(new CommDataContext(data));
	    byte[] processedDataBytes = processedData.getData();
	    byte[] encodedData = CryptoUtil.base64EncodeToBytes(processedDataBytes);
	    super.sendData(encodedData);
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
