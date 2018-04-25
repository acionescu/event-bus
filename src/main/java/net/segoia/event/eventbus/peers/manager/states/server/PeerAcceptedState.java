package net.segoia.event.eventbus.peers.manager.states.server;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.peers.PeerEventContext;
import net.segoia.event.eventbus.peers.PeerManager;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdRequest;
import net.segoia.event.eventbus.peers.events.auth.ServiceAccessIdRequestEvent;
import net.segoia.event.eventbus.peers.manager.states.PeerManagerState;

public class PeerAcceptedState extends PeerManagerState{

    @Override
    public void onEnterState(PeerManager peerManager) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onExitState(PeerManager peerManager) {
	// TODO Auto-generated method stub
	
    }

    @Override
    protected void registerLocalEventHandlers() {
	// TODO Auto-generated method stub
	
    }

    @Override
    protected void registerPeerEventHandlers() {
	registerPeerEventProcessor(ServiceAccessIdRequestEvent.class, (c)->{
	  handleServiceAccessIdRequest(c);
	});
	
    }
    
    protected void handleServiceAccessIdRequest(PeerEventContext<ServiceAccessIdRequestEvent> c) {
	
	
    }

}
