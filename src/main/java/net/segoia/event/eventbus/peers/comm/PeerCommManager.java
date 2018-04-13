package net.segoia.event.eventbus.peers.comm;

import net.segoia.event.eventbus.peers.security.CommDataContext;
import net.segoia.event.eventbus.peers.security.CommManager;
import net.segoia.event.eventbus.peers.security.CommOperationException;

public class PeerCommManager implements CommManager {
    public static final String DIRECT_COMM="DIRECT";
    public static final String SESSION_COMM="SESSION";
    
    private CommManager directCommManager;
    private CommManager sessionCommManager;

    @Override
    public CommDataContext processsOutgoingData(CommDataContext context) throws CommOperationException {
	return directCommManager.processsOutgoingData(context);
    }

    @Override
    public CommDataContext processIncomingData(CommDataContext context) throws CommOperationException {
	return directCommManager.processIncomingData(context);
    }

    
    public CommDataContext processsOutgoingSessionData(CommDataContext context) throws CommOperationException {
	return sessionCommManager.processsOutgoingData(context);
    }

    public CommDataContext processIncomingSessionData(CommDataContext context) throws CommOperationException {
	return sessionCommManager.processIncomingData(context);
    }

    public CommManager getDirectCommManager() {
        return directCommManager;
    }

    public void setDirectCommManager(CommManager directCommManager) {
        this.directCommManager = directCommManager;
    }

    public CommManager getSessionCommManager() {
        return sessionCommManager;
    }

    public void setSessionCommManager(CommManager sessionCommManager) {
        this.sessionCommManager = sessionCommManager;
    }
    
    
}
