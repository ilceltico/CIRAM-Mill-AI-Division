package it.unibo.ai.mulino.CIRAMill.domain;

import it.unibo.ai.mulino.CIRAMill.minimax.IAction;

public class BitBoardAction implements IAction {	
	private int from;
	private int to;
	private int remove;
	
	public BitBoardAction(int from, int to, int remove) {
		this.from = from;
		this.to = to;
		this.remove = remove;
	}
	
	public BitBoardAction() {
		this(0,0,0);
	}
	
	public String toString() {
		return BitBoardUtils.positionFromBoard(from) + BitBoardUtils.positionFromBoard(to) + BitBoardUtils.positionFromBoard(remove);
	}
	
}
