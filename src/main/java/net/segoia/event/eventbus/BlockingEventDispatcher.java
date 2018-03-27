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
 * This dispatcher will call the {@link EventContext#visitListener(EventContextListener)} method sequentially for all the
 * registered listeners </br>
 * All processing will be done in a single Thread, so each visitListener call will block execution until finished
 * 
 * @author adi
 *
 */
public class BlockingEventDispatcher extends SimpleEventDispatcher {
   
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    

    private ListMap<Integer, EventContextListener> pendingListeners = new ListMap<Integer, EventContextListener>(
	    new ListTreeMapFactory<Integer, EventContextListener>());

    public boolean dispatchEvent(EventContext ec) {
	
	ReadLock readLock = lock.readLock();
	
	readLock.lock();

	if (pendingListeners.size() > 0) {
	    
	    ListMap<Integer, EventContextListener> mergeListeners = new ListMap<Integer, EventContextListener>(
		    new ListTreeMapFactory<Integer, EventContextListener>());

	    mergeListeners.add(super.getListeners().getAll());
	    mergeListeners.add(pendingListeners.getAll());

	    pendingListeners.clear();
	    super.setListeners(mergeListeners);

	}

	try {
	    return super.dispatchEvent(ec);
	} finally {
	    readLock.unlock();
	}
    }

    public void registerListener(EventContextListener listener) {
	WriteLock writeLock = lock.writeLock();
	if (writeLock.tryLock()) {

//	    writeLock.lock();
	    super.registerListener(listener);
	    writeLock.unlock();
	} else {
	    pendingListeners.add(super.getListeners().size(), listener);
	}
    }

    public void removeListener(EventContextListener listener) {
	WriteLock writeLock = lock.writeLock();
	writeLock.lock();
	super.removeListener(listener);
	writeLock.unlock();
    }

    public void registerListener(EventContextListener listener, int priority) {
	WriteLock writeLock = lock.writeLock();

	ListMap<Integer, EventContextListener> recipient = super.getListeners();
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
	    recipient.add(super.getListeners().size(), listener);
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
