package net.segoia.event.eventbus.peers.manager.states;

public abstract class State<C> {

    protected abstract void onEnterState(StateContext<C> context);

    protected abstract void onExitState(StateContext<C> context);
}
