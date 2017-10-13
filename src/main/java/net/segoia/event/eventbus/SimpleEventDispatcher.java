/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.eventbus;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import net.segoia.util.data.ListMap;
import net.segoia.util.data.ListTreeMapFactory;

/**
 * This dispatcher will call the {@link EventContext#visitListener(EventListener)} method sequentially for all the
 * registered listeners </br>
 * All processing will be done in a single Thread, so each visitListener call will block execution until finished
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

    private ListMap<Integer, EventListener> pendingListeners = new ListMap<Integer, EventListener>(
	    new ListTreeMapFactory<Integer, EventListener>());

    public boolean dispatchEvent(EventContext ec) {
	if (lastError != null) {
	    return false;
	}
	ReadLock readLock = lock.readLock();
	
	readLock.lock();

	if (pendingListeners.size() > 0) {
	    
	    ListMap<Integer, EventListener> mergeListeners = new ListMap<Integer, EventListener>(
		    new ListTreeMapFactory<Integer, EventListener>());

	    mergeListeners.add(listeners.getAll());
	    mergeListeners.add(pendingListeners.getAll());

	    pendingListeners.clear();
	    listeners = mergeListeners;

	}

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
	if (writeLock.tryLock()) {

//	    writeLock.lock();
	    listeners.add(listeners.size(), listener);
	    writeLock.unlock();
	} else {

	    pendingListeners.add(listeners.size(), listener);
	}
    }

    public void removeListener(EventListener listener) {
	WriteLock writeLock = lock.writeLock();
	writeLock.lock();
	listeners.removeValue(listener);
	writeLock.unlock();
    }

    public void registerListener(EventListener listener, int priority) {
	WriteLock writeLock = lock.writeLock();

	ListMap<Integer, EventListener> recipient = listeners;
	boolean locked = false;
	if (writeLock.tryLock()) {
//	    writeLock.lock();
	    locked = true;
	} else {
	    recipient = pendingListeners;
	}
	if (priority >= 0) {
	    recipient.add(priority, listener);
	} else {
	    recipient.add(listeners.size(), listener);
	}
	if (locked) {
	    writeLock.unlock();
	}

    }

    @Override
    public void start() {
	// TODO Auto-generated method stub

    }

    @Override
    public void stop() {
	// TODO Auto-generated method stub

    }

}
