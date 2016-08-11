package net.segoia.event.eventbus;


public class DelegatingEventDispatcher extends EventDispatcherWrapper {
    private EventDispatcher delegateDispatcher;

    public DelegatingEventDispatcher(EventDispatcher nestedDispatcher, EventDispatcher delegateDispatcher) {
	super(nestedDispatcher);
	this.delegateDispatcher = delegateDispatcher;
    }

    @Override
    public boolean dispatchEvent(EventContext ec) {
	ec.setDelegateDispatcher(nestedDispatcher);
	return delegateDispatcher.dispatchEvent(ec);
    }

}
