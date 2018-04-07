package net.segoia.event.eventbus.peers.security;

public interface CommManager {
    public CommDataContext processsOutgoingData(CommDataContext context) throws CommOperationException;

    public CommDataContext processIncomingData(CommDataContext context) throws CommOperationException;

}
