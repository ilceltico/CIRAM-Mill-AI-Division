package it.unibo.ai.mulino.CIRAMill.domain;

import it.unibo.ai.mulino.CIRAMill.minimax.IAction;
import it.unibo.ai.mulino.CIRAMill.minimax.IHistoryTable;

public class BitBoardHistoryTable implements IHistoryTable {
	private long[][][] historyTable;
	
	public BitBoardHistoryTable() {
		// 25 perche' il 25esimo e' la posizione vuota
		historyTable = new long[25][24][25];
	}

	@Override
	public long getValue(IAction action) {
		BitBoardAction bAction = (BitBoardAction) action;
		
		return historyTable[bAction.getFrom() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getFrom())][Integer.numberOfTrailingZeros(bAction.getTo())][bAction.getRemove() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getRemove())];
	}

	@Override
	public void incrementValue(IAction action, long depth) {
		BitBoardAction bAction = (BitBoardAction) action;
		
		historyTable[bAction.getFrom() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getFrom())][Integer.numberOfTrailingZeros(bAction.getTo())][bAction.getRemove() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getRemove())] += depth * depth;
	}

}
