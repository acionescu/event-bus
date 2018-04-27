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

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This will receive events in an async manner, store them in an internal cache, and delegate them to the nested
 * dispatcher in one or more worker threads
 * 
 * @author adi
 *
 */
public class AsyncEventDispatcher extends EventDispatcherWrapper {

    private BlockingDeque<EventContext> eventQueue;
    private ThreadPoolExecutor threadPool;
    private Thread dispatcherThread;
    private boolean running;
    private boolean gracefullStop;

    /**
     * The future for the last event submitted for processing
     */
    private Future<?> lastFuture;

    private Object idleSignal = new Object();

    /**
     * This will only accept events if it is open
     */
    private boolean open;

    private long totalEventsQueued;

    private long totalEventsProcessed;

    /**
     * Builds an async {@link EventDispatcher} with a fixed capacity event cache and specified number of worker threads
     * 
     * @param nestedDispatcher
     *            - the internal dispatcher to delegate the events to
     * @param cacheCapacity
     * @param workerThreads
     */
    public AsyncEventDispatcher(EventDispatcher nestedDispatcher, int cacheCapacity, int workerThreads) {
	super(nestedDispatcher);
	this.nestedDispatcher = nestedDispatcher;
	eventQueue = new LinkedBlockingDeque<>(cacheCapacity);
	threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(workerThreads, new ThreadFactory() {
	    private ThreadFactory tf = Executors.defaultThreadFactory();

	    @Override
	    public Thread newThread(Runnable r) {
		Thread t = tf.newThread(r);
		/* No, we want this to be daemon */
		t.setDaemon(true);
		return t;
	    }
	});
    }

    /**
     * Builds an async {@link EventDispatcher} with given event cache capacity and one worker thread
     * 
     * @param nestedDispatcher
     * @param cacheCapacity
     */
    public AsyncEventDispatcher(EventDispatcher nestedDispatcher, int cacheCapacity) {
	this(nestedDispatcher, cacheCapacity, 1);
    }

    /**
     * Call this to start accepting events and dispatch them
     */
    public void start() {
	if (!running) {

	    dispatcherThread = new Thread(new Runnable() {
		@Override
		public void run() {
		    while (running) {

			// do {
			try {
			    /* get next event, or wait for one to become available */
			    EventContext ec = eventQueue.takeFirst();
			    if (!running) {
				if (!gracefullStop || (gracefullStop && eventQueue.isEmpty())) {
				    break;
				}
			    }

			    /* once we have an event, delegate it to the nested dispatcher in a worker thread */
			    lastFuture = threadPool.submit(new Runnable() {

				@Override
				public void run() {
				    // System.out.println(Thread.currentThread().getId() + ": Start processing "
				    // + (totalEventsProcessed + 1) + " " + ec.getEvent().getEt() + "
				    // "+threadPool.getCompletedTaskCount());

				    try {
					nestedDispatcher.dispatchEvent(ec);

				    } catch (Exception e) {

					// System.out.println("ERROR: " + e.getMessage());
					e.printStackTrace();
				    }
				    totalEventsProcessed = threadPool.getCompletedTaskCount() + 1;
				    // System.out
				    // .println("Processed " + totalEventsProcessed + "/" + totalEventsQueued);
				    // System.out.println("Thread pool stats " + threadPool.getCompletedTaskCount()
				    // + "/" + threadPool.getTaskCount());
				    if (totalEventsProcessed == totalEventsQueued) {
					notifyIdleWaiters();
				    }
				    // System.out.println("Remaining " + threadPool.getQueue().size());
				}
			    });

			} catch (InterruptedException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			// } while (!eventQueue.isEmpty());
		    }
		}
	    });

	    // dispatcherThread.setDaemon(false);

	    super.start();

	    /* make sure we accept events */
	    open();
	    /* we need to move to running state before starting the dispatcher thread */
	    running = true;
	    dispatcherThread.start();
	}
    }

    /**
     * Call this to start accepting events
     */
    public void open() {
	open = true;
    }

    public void stop() {

	running = false;
	open = false;
	try {
	    /* this is a hack to escape waiting on an empty queue */
	    eventQueue.putLast(new EventContext(null, null));
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	super.stop();
    }

    private void notifyIdleWaiters() {
	synchronized (idleSignal) {
	    idleSignal.notifyAll();
	}
    }

    public void waitToProcessAll() {
	synchronized (idleSignal) {
	    try {
		idleSignal.wait();
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    public void waitToProcessAll(int sleep) {
	long processed = totalEventsProcessed;
	do {

	    try {
		Thread.sleep(sleep);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    processed = totalEventsProcessed;
	} while (totalEventsQueued > totalEventsProcessed || processed < totalEventsProcessed);

	// if(totalEventsQueued < totalEventsProcessed) {
	// waitToProcessAll();
	// }

    }

    public void processAllAndStop() {
	open = false;
	gracefullStop = true;
	running = false;
	try {
	    /* this is a hack to escape waiting on an empty queue */
	    eventQueue.putLast(new EventContext(null, null));
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	try {
	    dispatcherThread.join();
	} catch (InterruptedException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	threadPool.shutdown();
	try {
	    threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	super.stop();
    }

    public boolean dispatchEvent(EventContext ec) {
	if (!open) {
	    throw new IllegalStateException("This is not open yet. Call open() or start()");
	}
	boolean posted = false;
	try {
	    eventQueue.putLast(ec);
	    posted = true;
	    totalEventsQueued++;
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return posted;
    }

}
