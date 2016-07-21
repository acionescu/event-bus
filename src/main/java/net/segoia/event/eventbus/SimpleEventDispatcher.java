package net.segoia.event.eventbus;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import net.segoia.util.data.ListMap;
import net.segoia.util.data.ListTreeMapFactory;

/**
 * This dispatcher will call the {@link EventListener#onEvent(EventContext)} method sequentially on all the registered
 * listeners </br>
 * All processing will be done in a single Thread, so each on onEvent call will block execution until finished
 * 
 * @author adi
 *
 */
public class SimpleEventDispatcher implements EventDispatcher {
    private boolean stopOnError = true;
    private Exception lastError;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private ListMap<Integer, EventListener> listeners = new ListMap<Integer, EventListener>(
	    new ListTreeMapFactory<Integer, EventListener>());

    public boolean dispatchEvent(EventContext ec) {
	if (lastError != null) {
	    return false;
	}
	ReadLock readLock = lock.readLock();
	readLock.lock();
	try {
	    for (List<EventListener> list : listeners.values()) {
		for (EventListener el : list) {
		    try {
			ec.visitListener(el);
		    } catch (Exception e) {

			if (stopOnError) {
			    e.printStackTrace();
			    lastError = e;
			    return false;
			} else {
			    e.printStackTrace();
			}
		    }
		}
	    }
	} finally {
	    readLock.unlock();
	}
	return true;
    }

    public void registerListener(EventListener listener) {
	WriteLock writeLock = lock.writeLock();
	writeLock.lock();
	listeners.add(listeners.size(), listener);
	writeLock.unlock();
    }

    public void removeListener(EventListener listener) {
	WriteLock writeLock = lock.writeLock();
	writeLock.lock();
	listeners.removeValue(listener);
	writeLock.unlock();
    }

    public void registerListener(EventListener listener, int priority) {
	WriteLock writeLock = lock.writeLock();
	writeLock.lock();
	listeners.add(priority, listener);
	writeLock.unlock();

    }

}
