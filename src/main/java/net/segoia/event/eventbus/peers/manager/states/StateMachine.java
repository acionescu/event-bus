package net.segoia.event.eventbus.peers.manager.states;

public class StateMachine {
    private State state;
    
    public <C> void goToState(State<C> newState, C context) {
	StateContext<?> stateContext = new StateContext<>(this, context);
	
	if(state != null) {
	    state.onExitState(stateContext);
	}
	state = newState;
	state.onEnterState(stateContext);
    }
}
