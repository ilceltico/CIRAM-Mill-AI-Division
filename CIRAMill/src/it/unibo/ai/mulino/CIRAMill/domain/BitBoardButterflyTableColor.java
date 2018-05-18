package it.unibo.ai.mulino.CIRAMill.domain;

import it.unibo.ai.mulino.CIRAMill.minimax.IAction;
import it.unibo.ai.mulino.CIRAMill.minimax.IHistoryTable;
import it.unibo.ai.mulino.CIRAMill.minimax.IHistoryTableColor;

public class BitBoardButterflyTableColor implements IHistoryTableColor {
private long[][][][] butterflyTable;
	
	public BitBoardButterflyTableColor() {
		// 25 perche' il 25esimo e' la posizione vuota
		butterflyTable = new long[2][25][24][25];

		for(int i=0; i<25; i++) {
			for(int j=0; j<24; j++) {
				for(int k=0; k<25; k++) {
					butterflyTable[0][i][j][k] = 1;
					butterflyTable[1][i][j][k] = 1;
				}
			}
		}
	}

	@Override
	public long getValue(IAction action, byte player) {
		BitBoardAction bAction = (BitBoardAction) action;
		
		return butterflyTable[player][bAction.getFrom() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getFrom())][Integer.numberOfTrailingZeros(bAction.getTo())][bAction.getRemove() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getRemove())];
	}

	@Override
	public void incrementValue(IAction action, long depth, byte player) {
		BitBoardAction bAction = (BitBoardAction) action;
		
		butterflyTable[player][bAction.getFrom() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getFrom())][Integer.numberOfTrailingZeros(bAction.getTo())][bAction.getRemove() == 0 ? 24 : Integer.numberOfTrailingZeros(bAction.getRemove())] += 1;
	}

}
