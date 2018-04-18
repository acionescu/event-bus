package net.segoia.event.eventbus.peers.manager.states.server;

import net.segoia.event.eventbus.peers.PeerManager;
import net.segoia.event.eventbus.peers.manager.states.PeerManagerState;

public class PeerBindRequestedState extends PeerManagerState{

    @Override
    public void onEnterState(PeerManager peerManager) {
	//TODO: check if we allow this
	
	peerManager.goToState(PeerManager.PEER_BIND_ACCEPTED);
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
	
    }

}
