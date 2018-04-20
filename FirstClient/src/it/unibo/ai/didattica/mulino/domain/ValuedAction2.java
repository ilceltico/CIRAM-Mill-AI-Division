package it.unibo.ai.didattica.mulino.domain;

import it.unibo.ai.didattica.mulino.actions.Action;

public class ValuedAction2 {
	
	private Action action;
	private int value;
	private State newState;
	
	public ValuedAction2(Action action, int value, State newState) {
		this.action = action;
		this.value = value;
		this.newState = newState;
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public State getNewState() {
		return this.newState;
	}
	
	@Override
	public String toString() {
		return (action!=null?action.toString():"") + ", value: " + value;
//		return action.toString() + ", value:" + value;
	}

}
