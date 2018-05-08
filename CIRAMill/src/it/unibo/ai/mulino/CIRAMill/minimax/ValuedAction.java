package it.unibo.ai.mulino.CIRAMill.minimax;

public class ValuedAction {
	private IAction action;
	private int value;
	
	public ValuedAction() {
		this(null, 0);
	}
	
	public ValuedAction(IAction action, int value) {
		this.action = action;
		this.value = value;
	}
	
	public void set (IAction action, int value) {
		this.action = action;
		this.value = value;
	}
	
	public IAction getAction() {
		return this.action;
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return this.action.toString() + ", value: " + value;
	}
}