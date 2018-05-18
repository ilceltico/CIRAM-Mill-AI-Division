package it.unibo.ai.mulino.CIRAMill.domain;

import it.unibo.ai.mulino.CIRAMill.minimax.IAction;
import it.unibo.ai.mulino.CIRAMill.minimax.IHistoryTable;

public class BitBoardButterflyTable implements IHistoryTable {
private long[][][] butterflyTable;
	
	public BitBoardButterflyTable() {
		// 25 perche' il 25esimo e' la posizione vuota
		butterflyTable = new long[25][24][25];
		
		for(int i=0; i<25; i++) {
			for(int j=0; j<24; j++) {
				for(int k=0; k<25; k++) {
					butterflyTable[i][j][k] = 1;
				}
			}
		}
	}

	@Override
	public long getValue(IAction action) {
		BitBoardAction bAction = (BitBoardAction) action;
		
		return butterflyTable[bAction.getFrom() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getFrom())][Integer.numberOfTrailingZeros(bAction.getTo())][bAction.getRemove() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getRemove())];
	}

	@Override
	public void incrementValue(IAction action, long depth) {
		BitBoardAction bAction = (BitBoardAction) action;
		
		butterflyTable[bAction.getFrom() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getFrom())][Integer.numberOfTrailingZeros(bAction.getTo())][bAction.getRemove() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getRemove())] += 1;
	}

}
