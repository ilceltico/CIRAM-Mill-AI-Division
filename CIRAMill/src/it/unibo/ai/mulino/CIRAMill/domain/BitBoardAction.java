package it.unibo.ai.mulino.CIRAMill.domain;

public class BitBoardAction {	
	private int from;
	private int to;
	private int remove;
	private int player;
	
	// serve la fase?
//	private byte phase;
	
	public BitBoardAction(int from, int to, int remove, byte player) {
		this.from = from;
		this.to = to;
		this.remove = remove;
		this.player = player;
	}
	
	// che valore di default metto per 'player'?
	public BitBoardAction() {
		this(0,0,0,(byte) 0);
	}
	
}
