package net.segoia.event.eventbus;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;

/**
 * This will receive events in an async manner, store them in an internal cache, and delegate them to the nested
 * dispatcher in one or more worker threads
 * 
 * @author adi
 *
 */
public class AsyncEventDispatcher extends EventDispatcherWrapper {

    private BlockingDeque<EventContext> eventQueue;
    private ExecutorService threadPool;
    private Thread dispatcherThread;
    private boolean running;

    /**
     * This will only accept events if it is open
     */
    private boolean open;

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
	threadPool = Executors.newFixedThreadPool(workerThreads, new ThreadFactory() {
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
			try {
			    /* get next event, or wait for one to become available */
			    EventContext ec = eventQueue.takeFirst();
			    if (!running) {
				break;
			    }
			    /* once we have an event, delegate it to the nested dispatcher in a worker thread */
			    threadPool.submit(new Runnable() {

				@Override
				public void run() {
				    nestedDispatcher.dispatchEvent(ec);

				}
			    });

			} catch (InterruptedException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}

		    }
		    System.out.println("Exiting");
		}
	    });
	    
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

    public boolean dispatchEvent(EventContext ec) {
	if (!open) {
	    throw new IllegalStateException("This is not open yet. Call open() or start()");
	}
	boolean posted = false;
	try {
	    eventQueue.putLast(ec);
	    posted = true;
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return posted;
    }

}
