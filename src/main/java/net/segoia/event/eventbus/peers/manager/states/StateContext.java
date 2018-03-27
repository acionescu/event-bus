package net.segoia.event.eventbus.peers.manager.states;

public class StateContext<C> {
    private StateMachine stateMachine;
    private C customContext;

    public StateContext(StateMachine stateMachine, C customContext) {
	super();
	this.stateMachine = stateMachine;
	this.customContext = customContext;
    }

    public void goToState(State<C> newState, C context) {
	stateMachine.goToState(newState, context);
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public C getCustomContext() {
        return customContext;
    }
    
    
}
