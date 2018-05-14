package it.unibo.ai.mulino.CIRAMill.minimax;

public interface ITranspositionTable {

	public ValuedAction[] getValuedActions(IState state);
	public void putValuedAction(IState state, ValuedAction valuedAction, int depth);
	
}
