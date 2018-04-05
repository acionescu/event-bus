package net.segoia.event.eventbus.peers.comm;

import java.util.List;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.DefaultEventRelay;
import net.segoia.event.eventbus.peers.EventTransceiver;
import net.segoia.event.eventbus.peers.PeerContext;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentity;
import net.segoia.event.eventbus.peers.events.auth.id.NodeIdentityType;

public class CommProtocolRelay extends DefaultEventRelay {
    private PeerContext peerContext;
    private CommunicationProtocol commProtocol;
    private NodeCommunicationStrategy txStrategy = null;
    private NodeCommunicationStrategy rxStrategy = null;
    private int ourIdentityIndex;
    private int peerIdentityIndex;

    public CommProtocolRelay(String id, EventTransceiver transceiver, PeerContext peerContext) {
	super(id, transceiver);
	this.peerContext = peerContext;
	this.commProtocol = peerContext.getCommProtocol();
    }

    @Override
    public void init() {
	super.init();

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

    }

    protected Event getSendEvent(Event event) {
	
    }

}
