package it.unibo.ai.mulino.CIRAMill.minimax;

public interface ITranspositionTable {

	public IAction[] getActions(IState state);
	public void putAction(IState state, IAction action, int depth);
	public void clear();
}
