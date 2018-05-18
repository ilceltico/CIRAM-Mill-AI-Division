package it.unibo.ai.mulino.CIRAMill.domain;

import it.unibo.ai.mulino.CIRAMill.minimax.IAction;
import it.unibo.ai.mulino.CIRAMill.minimax.IHistoryTable;
import it.unibo.ai.mulino.CIRAMill.minimax.IHistoryTableColor;

public class BitBoardHistoryTableColor implements IHistoryTableColor {
	private long[][][][] historyTable;
	
	public BitBoardHistoryTableColor() {
		// 25 perche' il 25esimo e' la posizione vuota
		historyTable = new long[2][25][24][25];
	}

	@Override
	public long getValue(IAction action, byte player) {
		BitBoardAction bAction = (BitBoardAction) action;
		
		return historyTable[player][bAction.getFrom() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getFrom())][Integer.numberOfTrailingZeros(bAction.getTo())][bAction.getRemove() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getRemove())];
	}

	@Override
	public void incrementValue(IAction action, long depth, byte player) {
		BitBoardAction bAction = (BitBoardAction) action;
		
		historyTable[player][bAction.getFrom() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getFrom())][Integer.numberOfTrailingZeros(bAction.getTo())][bAction.getRemove() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getRemove())] += depth * depth;
	}

}
