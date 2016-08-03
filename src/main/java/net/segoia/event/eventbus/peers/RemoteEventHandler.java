package net.segoia.event.eventbus.peers;

public interface RemoteEventHandler<N extends EventNode> {
     void handleRemoteEvent(RemoteEventContext<N> rec);
}
