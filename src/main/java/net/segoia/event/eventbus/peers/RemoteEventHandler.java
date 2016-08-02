package net.segoia.event.eventbus.peers;

public interface RemoteEventHandler<N extends EventBusNode> {
     void handleRemoteEvent(RemoteEventContext<N> rec);
}
