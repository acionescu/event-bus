package net.segoia.event.eventbus.peers.manager.states;

import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.FilteringEventProcessor;
import net.segoia.event.eventbus.PassthroughCustomEventContextListenerFactory;
import net.segoia.event.eventbus.peers.PeerContextHandler;
import net.segoia.event.eventbus.peers.PeerEventContext;
import net.segoia.event.eventbus.peers.PeerManager;

public abstract class PeerManagerState {
    private FilteringEventProcessor localEventsProcessor = new FilteringEventProcessor(new PassthroughCustomEventContextListenerFactory());
    private FilteringEventProcessor peerEventsProcessor = new FilteringEventProcessor(new PassthroughCustomEventContextListenerFactory());

    public PeerManagerState() {
	init();
    }

    private void init() {
	registerLocalEventHandlers();
	registerPeerEventHandlers();
    }

    public abstract void onEnterState(PeerManager peerManager);

    public abstract void onExitState(PeerManager peerManager);

    protected abstract void registerLocalEventHandlers();

    protected abstract void registerPeerEventHandlers();

    public <E extends Event> boolean handleEventFromPeer(PeerEventContext<E> ec) {
	return peerEventsProcessor.processEvent(ec);
    }

    protected <E extends Event> void registerLocalEventProcessor(Class<E> clazz, PeerContextHandler<E> handler) {
	localEventsProcessor.addEventHandler(clazz, (c) -> {
	    handler.handleEvent((PeerEventContext<E>) c);
	});
    }

    protected <E extends Event> void registerPeerEventProcessor(Class<E> clazz, PeerContextHandler<E> handler) {
	peerEventsProcessor.addEventHandler(clazz, (c) -> {
	    handler.handleEvent((PeerEventContext<E>) c);
	});
    }
    
    protected void registerPeerEventProcessor(String eventType, PeerContextHandler<Event> handler) {
	peerEventsProcessor.addEventHandler(eventType, (c)->{
	    handler.handleEvent((PeerEventContext<Event>)c);
	});
    }
    
    protected void registerPeerEventProcessor(PeerContextHandler<Event> handler) {
	peerEventsProcessor.addEventHandler((c)->{
	    handler.handleEvent((PeerEventContext<Event>)c);
	});
    }

}
