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
	
	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public int getRemove() {
		return remove;
	}

	public String toString() {
		return BitBoardUtils.positionFromBoard(from) + BitBoardUtils.positionFromBoard(to) + BitBoardUtils.positionFromBoard(remove);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(getClass() != obj.getClass())
			return false;
				
		return	this.from == ((BitBoardAction) obj).getFrom() &&
				this.to == ((BitBoardAction) obj).getTo() &&
				this.remove == ((BitBoardAction) obj).getRemove();
	}
	
}
