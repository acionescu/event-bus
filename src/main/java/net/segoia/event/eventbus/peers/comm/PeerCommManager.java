package net.segoia.event.eventbus.peers.comm;

import net.segoia.event.eventbus.peers.security.CommDataContext;
import net.segoia.event.eventbus.peers.security.CommManager;
import net.segoia.event.eventbus.peers.security.CommOperationException;

public class PeerCommManager implements CommManager {
    private CommManager activeCommManager;

    @Override
    public CommDataContext processsOutgoingData(CommDataContext context) throws CommOperationException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public CommDataContext processIncomingData(CommDataContext context) throws CommOperationException {
	// TODO Auto-generated method stub
	return null;
    }

    public CommManager getActiveCommManager() {
	return activeCommManager;
    }

    public void setActiveCommManager(CommManager activeCommManager) {
	this.activeCommManager = activeCommManager;
    }

}
