package net.segoia.event.eventbus;

import java.util.concurrent.Future;

public class AsyncEventTracker extends EventTracker {
    private Future<Void> future;

    public AsyncEventTracker(Future<Void> future, boolean posted) {
	super(posted);
	this.future = future;

    }

    public boolean isDone() {
	return future.isDone();
    }

}
