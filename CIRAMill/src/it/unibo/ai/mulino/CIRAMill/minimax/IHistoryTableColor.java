package it.unibo.ai.mulino.CIRAMill.minimax;

public interface IHistoryTableColor {
	public long getValue(IAction action, byte player);
	public void incrementValue(IAction action, long depth, byte player);
}
