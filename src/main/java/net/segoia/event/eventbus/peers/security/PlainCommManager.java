package net.segoia.event.eventbus.peers.security;

public class PlainCommManager implements CommManager{

    @Override
    public CommDataContext processsOutgoingData(CommDataContext context) throws CommOperationException {
	return context;
    }

    @Override
    public CommDataContext processIncomingData(CommDataContext context) throws CommOperationException {
	return context;
    }

}
