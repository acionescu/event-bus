package net.segoia.event.eventbus;

import java.util.ArrayList;
import java.util.List;

import net.segoia.util.data.ListMap;
import net.segoia.util.data.ListTreeMapFactory;

public class SimpleEventDispatcher implements EventDispatcher {
    private boolean stopOnError = true;
    private Throwable lastError;
    private List<Throwable> errors = new ArrayList<>();
    private ListMap<Integer, EventContextListener> listeners = new ListMap<Integer, EventContextListener>(
	    new ListTreeMapFactory<Integer, EventContextListener>());

    @Override
    public void start() {
	// TODO Auto-generated method stub

    }

    @Override
    public void stop() {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean dispatchEvent(EventContext ec) {
	if (lastError != null && stopOnError) {
	    throw new RuntimeException("Dispatcher is stopped due to error", lastError);
	}

	/* clear errors before dispatch */
	errors.clear();

	for (List<EventContextListener> list : listeners.values()) {
	    for (EventContextListener el : list) {
		try {
		    // System.out.println("visiting listener "+el+" for event "+ec.getEvent().getEt());
		    ec.visitListener(el);
		} catch (Throwable e) {
		    lastError = e;
		    if (stopOnError) {
			System.err.println("Dispatcher stopping due to errror "+e.getMessage());
			return false;
		    }
		}
	    }
	}
	return true;
    }

    @Override
    public void registerListener(EventContextListener listener) {
	registerListener(listener, listeners.size());
    }

    @Override
    public void registerListener(EventContextListener listener, int priority) {
	listeners.add(priority, listener);
    }

    @Override
    public void removeListener(EventContextListener listener) {
	listeners.removeValue(listener);

    }

    public boolean isStopOnError() {
	return stopOnError;
    }

    public void setStopOnError(boolean stopOnError) {
	this.stopOnError = stopOnError;
    }

    public Throwable getLastError() {
	return lastError;
    }

    public void setLastError(Throwable lastError) {
	this.lastError = lastError;
    }

    public List<Throwable> getErrors() {
	return errors;
    }

    public void setErrors(List<Throwable> errors) {
	this.errors = errors;
    }

    public ListMap<Integer, EventContextListener> getListeners() {
	return listeners;
    }

    public void setListeners(ListMap<Integer, EventContextListener> listeners) {
	this.listeners = listeners;
    }

}
