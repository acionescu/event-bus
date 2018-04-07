package net.segoia.event.eventbus.peers.comm;

import net.segoia.event.eventbus.peers.ChainedEventTransceiver;
import net.segoia.event.eventbus.peers.EventTransceiver;
import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.security.CommDataContext;
import net.segoia.event.eventbus.peers.security.CommManager;
import net.segoia.event.eventbus.peers.security.CommOperationException;
import net.segoia.event.eventbus.peers.security.PeerCommContext;

public class CommProtocolEventTransceiver extends ChainedEventTransceiver {
    private PeerContext peerContext;
    private CommunicationProtocol commProtocol;
    private NodeCommunicationStrategy txStrategy = null;
    private NodeCommunicationStrategy rxStrategy = null;
    private int ourIdentityIndex;
    private int peerIdentityIndex;

    /**
     * The communication manager that implements the communication protocol settled with the peer
     */
    private CommManager commManager;

    public CommProtocolEventTransceiver(EventTransceiver downstream, PeerContext peerContext) {
	super(downstream);
	this.peerContext = peerContext;
	init();
    }

    @Override
    protected void init() {
	super.init();
	
	commProtocol = peerContext.getCommProtocol();
	CommunicationProtocolDefinition protocolDefinition = commProtocol.getProtocolDefinition();
	NodeCommunicationStrategy clientCommStrategy = protocolDefinition.getClientCommStrategy();
	NodeCommunicationStrategy serverCommStrategy = protocolDefinition.getServerCommStrategy();

	CommunicationProtocolConfig protocolConfig = commProtocol.getConfig();

	if (peerContext.isInServerMode()) {
	    /* we're acting as client */
	    txStrategy = clientCommStrategy;
	    rxStrategy = serverCommStrategy;

	    ourIdentityIndex = protocolConfig.getClientNodeIdentity();
	    peerIdentityIndex = protocolConfig.getServerNodeIdentity();
	} else {
	    /* we're acting as server */
	    txStrategy = serverCommStrategy;
	    rxStrategy = clientCommStrategy;

	    peerIdentityIndex = protocolConfig.getClientNodeIdentity();
	    ourIdentityIndex = protocolConfig.getServerNodeIdentity();
	}

	/* obtain a communication manager for this peer */
	commManager = peerContext.getNodeContext().getSecurityManager().getCommManager(
		new PeerCommContext(ourIdentityIndex, peerIdentityIndex, txStrategy, rxStrategy, peerContext));

    }

    @Override
    public void onPeerData(byte[] data) {
	CommDataContext processedData;
	try {
	    processedData = commManager.processIncomingData(new CommDataContext(data));
	    receiveData(processedData.getData());
	} catch (CommOperationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }
    
    

    @Override
    public void sendData(byte[] data) {
	try {
	    CommDataContext processedData = commManager.processsOutgoingData(new CommDataContext(data));
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
