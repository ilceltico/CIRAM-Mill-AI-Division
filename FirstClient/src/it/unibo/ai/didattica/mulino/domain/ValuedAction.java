package it.unibo.ai.didattica.mulino.domain;

import it.unibo.ai.didattica.mulino.actions.Action;

public class ValuedAction {
	
	private Action action;
	private int value;
	
	public ValuedAction(Action action, int value) {
		this.action = action;
		this.value = value;
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return (action!=null?action.toString():"") + ", value: " + value;
//		return action.toString() + ", value:" + value;
	}

}
