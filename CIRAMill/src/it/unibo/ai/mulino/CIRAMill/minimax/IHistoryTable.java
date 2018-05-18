package it.unibo.ai.mulino.CIRAMill.minimax;

public interface IHistoryTable {
	public long getValue(IAction action);
	public void incrementValue(IAction action, long depth);
}
