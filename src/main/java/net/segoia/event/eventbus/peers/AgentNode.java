package net.segoia.event.eventbus.peers;

import net.segoia.event.conditions.Condition;
import net.segoia.event.conditions.EventClassMatchCondition;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.conditions.TrueCondition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.FilteringEventBus;

/**
 * An agent is a node that reacts to events in a certain way </br>
 * It can also post events as a public node, or hiding behind a relay node
 * 
 * @author adi
 *
 */
public abstract class AgentNode extends EventNode {
    protected EventNode mainNode;
    /**
     * if true, it will call {@link #init()} from the constructor, otherwise somebody else will have to call
     * {@link #lazyInit()}
     */
    private boolean autoinit = true;
    private boolean initialized;

    /**
     * This is used to delegate events to internal handlers
     */
    private FilteringEventBus handlersBus;

    public AgentNode() {
	this(true);
    }

    public AgentNode(boolean autoinit) {

	this.autoinit = autoinit;

	if (autoinit) {
	    init();
	}
    }

    protected void init() {
	registerHandlers();
	agentConfig();
	setRequestedEventsCondition();
	agentInit();
	initialized = true;
    }

    public void lazyInit() {
	if (!autoinit && !initialized) {
	    init();
	}
    }

    private void initInternalBus() {
	if (handlersBus == null) {
	    handlersBus = new FilteringEventBus();
	}
    }

    protected abstract void agentInit();

    protected abstract void agentConfig();

    /**
     * Override this to register handlers
     */
    protected abstract void registerHandlers();

    protected void setRequestedEventsCondition() {
	// TODO: implement this, for now request all
	config.setDefaultRequestedEvents(new TrueCondition());
    }

    @Override
    protected EventRelay buildLocalRelay(String peerId) {
	return new DefaultEventRelay(peerId, this);
    }

    protected void removeEventHandler(CustomEventHandler<?> handler) {
	handlersBus.removeListener(handler);
    }

    protected void addEventHandler(Class<?> eventClass, CustomEventHandler<?> handler) {
	addBusHandler(new EventClassMatchCondition(eventClass), handler);
    }

    protected void addEventHandler(String eventType, CustomEventHandler<?> handler) {
	addBusHandler(new StrictEventMatchCondition(eventType), handler);
    }
    
    protected <E extends Event> void addEventHandler(Class<E> eventClass, EventHandler<E> handler) {
	addEventHandler(eventClass, new CustomEventHandler<>(handler));
    }
    
    protected <E extends Event> void addEventHandler(String eventType, EventHandler<E> handler) {
	addEventHandler(eventType, new CustomEventHandler<>(handler));
    }

    private void addBusHandler(Condition cond, CustomEventHandler<?> handler) {
	initInternalBus();
	handlersBus.registerListener(cond, handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.segoia.event.eventbus.peers.EventBusNode#handleRemoteEvent(net.segoia.event.eventbus.peers.PeerEventContext)
     */
    @Override
    protected void handleRemoteEvent(PeerEventContext pc) {
	Event event = pc.getEvent();
	//
	// RemoteEventHandler<?> handler = handlersByEventClass.get(event.getClass());
	//
	// if (handler == null) {
	// String et = event.getEt();
	// handler = handlers.get(et);
	// }
	//
	// if (handler != null) {
	// handler.handleRemoteEvent(new RemoteEventContext(this, pc));
	// }

	if (handlersBus != null) {
	    handlersBus.postEvent(event);
	}

	handleEvent(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.segoia.event.eventbus.peers.EventNode#onPeerLeaving(java.lang.String)
     */
    @Override
    public void onPeerLeaving(String peerId) {
	super.onPeerLeaving(peerId);
	/* since an agent is bound by the main node, if that leaves we need to finish too */
	terminate();
    }

}
