package it.unibo.ai.mulino.CIRAMill.domain;

public class BitBoardAction {
	
	// i valori sono dati in base 10 (non sono le bitboard)
	
	// bisogna trovare un valore 'null' per le posizioni
	private byte from;
	private byte to;
	private byte remove;
	private byte player;
	
	// serve la fase?
//	private byte phase;
	
	public BitBoardAction(String from, String to, String remove, byte player) {
		
	}
	
	public BitBoardAction(byte from, byte to, byte remove, byte player) {
		
	}
	
	public BitBoardAction(int from, int to, int remove, byte player) {
	
	}
	
	public BitBoardAction() {
		
	}
	
}
