package it.unibo.ai.mulino.CIRAMill.domain;

import java.util.ArrayList;
import java.util.HashMap;

import it.unibo.ai.didattica.mulino.domain.State;

public class BitBoardState {
	
	public static final byte DEFAULT_INITIAL_CHECKERS = 9;
	
	public static final byte WHITE = 0;
	public static final byte BLACK = 1;
	
	public static final byte INITIALPHASE = 10;
	public static final byte MIDGAME = 11;
	
	private int[] board = new int[2];
	private byte[] checkersToPut = new byte[2];
	private byte[] checkersOnBoard = new byte[2];
	private byte playerToMove;
	private byte gamePhase;
	
	
	public BitBoardState(byte initialWhiteCheckers, byte initialBlackCheckers) {
		if (initialWhiteCheckers < 1 || initialBlackCheckers < 1)
			throw new IllegalArgumentException("Initial checkers must be positive");
		board[WHITE] = 0;
		board[BLACK] = 0;
		
		checkersToPut[WHITE] = initialWhiteCheckers;
		checkersToPut[BLACK] = initialBlackCheckers;
		
		checkersOnBoard[WHITE] = 0;
		checkersOnBoard[BLACK] = 0;
		
		playerToMove = WHITE;
		
		gamePhase = INITIALPHASE;
	}
	
	public BitBoardState(int initialWhiteCheckers, int initialBlackCheckers) {
		this((byte) checkIntToByte(initialWhiteCheckers), (byte) checkIntToByte(initialBlackCheckers));
	}
	
	public BitBoardState() {
		this(DEFAULT_INITIAL_CHECKERS, DEFAULT_INITIAL_CHECKERS);
	}
	
	public static int checkIntToByte(int value) {
		if (value > Byte.MAX_VALUE)
			throw new IllegalArgumentException("Initial checkers must be less or equal to  " + Byte.MAX_VALUE);
		return value;
	}

	
	public static BitBoardState fromStateToBitBoard(State state, byte playerToMove) throws Exception {
		BitBoardState result = new BitBoardState();
		
		HashMap<String, State.Checker> boardMap = state.getBoard();
		for (String position : boardMap.keySet()) {
			if (boardMap.get(position) == State.Checker.WHITE)
				result.board[WHITE] |= BitBoardUtils.boardFromPosition(position);
			else if (boardMap.get(position) == State.Checker.BLACK)
				result.board[BLACK] |= BitBoardUtils.boardFromPosition(position);
		}
		
		if (state.getWhiteCheckers() > Byte.MAX_VALUE || state.getWhiteCheckers() < 0)
			throw new Exception("Wrong number of White checkers to put");
		result.checkersToPut[WHITE] = (byte) state.getWhiteCheckers();
		
		if (state.getBlackCheckers() > Byte.MAX_VALUE || state.getBlackCheckers() < 0)
			throw new Exception("Wrong number of Black checkers to put");
		result.checkersToPut[BLACK] = (byte) state.getBlackCheckers();
		
		if (state.getWhiteCheckersOnBoard() > Byte.MAX_VALUE || state.getWhiteCheckersOnBoard() < 0)
			throw new Exception("Wrong number of White checkers on board");
		result.checkersOnBoard[WHITE] = (byte) state.getWhiteCheckersOnBoard();
		
		if (state.getBlackCheckersOnBoard() > Byte.MAX_VALUE || state.getBlackCheckersOnBoard() < 0)
			throw new Exception("Wrong number of Black checkers on board");
		result.checkersOnBoard[BLACK] = (byte) state.getBlackCheckersOnBoard();
		
		if (playerToMove > BitBoardState.BLACK)
			throw new Exception("Wrong player");
		result.playerToMove = playerToMove;
		
		switch (state.getCurrentPhase()) {
		case FIRST: result.gamePhase = INITIALPHASE; break;
		case SECOND: result.gamePhase = BitBoardState.MIDGAME; break;
		case FINAL: result.gamePhase = BitBoardState.MIDGAME; break;
		default: throw new Exception("Wrong Phase");
		}
		
		return result;
	}
	
	/*
	 * SECONDO ME: (dovrebbe essere giusto anyway)
	 * 
	 * calcolando le mosse possibili si hanno in automatico anche i successori in quanto,
	 * data un azione, lo stato successivo è dato da:
	 * 
	 * newState ^= action.getFrom
	 * newState |= action.getTo
	 * newState ^= action.getRemove
	 * 
	 * questa codifica potrebbe aiutare a definire valori 'null'
	 * per le azioni (vedi la classe BitBoardAtion)
	 */
	
	public ArrayList<BitBoardAction> getFollowingMoves() throws Exception {
		ArrayList<BitBoardAction> result = new ArrayList<>();
		
		switch (gamePhase) {
		case INITIALPHASE:
			result = getFollowingMovesInitialPhase();
			break;
		case MIDGAME:
			if(playerToMove == WHITE) {
				if(checkersOnBoard[WHITE] > 3) {
					result = getFollowingMovesMidGame();
				} else {
					result = getFollowingMovesEndGame();
				}
			} else if(playerToMove == BLACK) {
				if(checkersOnBoard[BLACK] > 3) {
					result = getFollowingMovesMidGame();
				} else {
					result = getFollowingMovesEndGame();
				}
			}
			break;
		default:
			throw new Exception("Wrong Phase");
		}
		
		return result;
	}
	
	private ArrayList<BitBoardAction> getFollowingMovesInitialPhase() {
		ArrayList<BitBoardAction> result = new ArrayList<>();
		BitBoardAction temp = new BitBoardAction();
		int from;
		int to;
		int remove;
		byte opponentPlayer = playerToMove == BitBoardState.WHITE ? BitBoardState.BLACK : BitBoardState.WHITE;
		
		for(int i=0; i<24; i++) {
			// empty position
			if(((board[WHITE] | board[BLACK]) & (1 << i)) == 0) {
				to = i;
				
				if(BitBoardUtils.hasCompletedMorris(board, i, playerToMove)) {
					boolean foundRemovableChecker = false;
				
					for(int j=0; j<24; j++) {
						// opponent checker
						if((board[opponentPlayer] & (1 << j)) != 0 && !BitBoardUtils.hasCompletedMorris(board, j, opponentPlayer)) {
							remove = j;
							temp = new BitBoardAction(-1, to, remove, playerToMove);
							result.add(temp);
							foundRemovableChecker = true;
						}							
					}
					if(!foundRemovableChecker) {
						for(int j=0; j<24; j++) {
							// opponent checker
							if((board[opponentPlayer] & (1 << j)) != 0 && BitBoardUtils.hasCompletedMorris(board, j, opponentPlayer)) {
								remove = j;
								temp = new BitBoardAction(-1, to, remove, playerToMove);
								result.add(temp);
							}
						}
					}
				} else {
					temp = new BitBoardAction(-1, to, -1, playerToMove);
					result.add(temp);
				}
			}
			
			/*
			 * resetto from, to e remove?
			 */
			
		}
		
		return result;
	}
	
	private ArrayList<BitBoardAction> getFollowingMovesMidGame() {
		ArrayList<BitBoardAction> result = new ArrayList<>();
		BitBoardAction temp = new BitBoardAction();
		int from;
		int to;
		int remove;
		byte opponentPlayer = playerToMove == BitBoardState.WHITE ? BitBoardState.BLACK : BitBoardState.WHITE;
		
		for(int i=0; i<24; i++) {
			// player checker
			if((board[playerToMove] & (i << i)) != 0) {
				from = i;
				
				for(Integer adjacentPosition : BitBoardUtils.getAdjacentPositions(i)) {
					// empty pos
					if(((board[WHITE] | board[BLACK]) & (1 << adjacentPosition)) == 0) {
						to = adjacentPosition;
						
						if(BitBoardUtils.hasCompletedMorris(board, adjacentPosition, playerToMove)) {
							boolean foundRemovableChecker = false;
							
							for(int j=0; j<24; j++) {
								// opponent checker
								if((board[opponentPlayer] & (1 << j)) != 0 && !BitBoardUtils.hasCompletedMorris(board, j, opponentPlayer)) {
									remove = j;
									temp = new BitBoardAction(from, to, remove, playerToMove);
									result.add(temp);
									foundRemovableChecker = true;
								}
							}
							
							if(!foundRemovableChecker) {
								for(int j=0; j<24; j++) {
									// opponent checker
									if((board[opponentPlayer] & (1 << j)) != 0 && BitBoardUtils.hasCompletedMorris(board, j, opponentPlayer)) {
										remove = j;
										temp = new BitBoardAction(from, to, remove, playerToMove);
										result.add(temp);
									}
								}
							}							
						} else {
							temp= new BitBoardAction(from, to, -1, playerToMove);
							result.add(temp);
						}
					}
				}
			}

			/*
			 * resetto from, to e remove?
			 */
			
		}
		
		return result;
	}
	
	private ArrayList<BitBoardAction> getFollowingMovesEndGame() {
		ArrayList<BitBoardAction> result = new ArrayList<>();
		BitBoardAction temp = new BitBoardAction();
		int from;
		int to;
		int remove;
		byte opponentPlayer = playerToMove == BitBoardState.WHITE ? BitBoardState.BLACK : BitBoardState.WHITE;
		
		for(int i=0; i<24; i++) {
			// player checker
			if((board[playerToMove] & (i << i)) != 0) {
				from = i;
				
				for(int j=0; j<24; j++) {
					// empty position
					if(((board[WHITE] | board[BLACK]) & (1 << j)) == 0) {
						to = j;
						
						if(BitBoardUtils.hasCompletedMorris(board, j, playerToMove)) {
							boolean foundRemovableChecker = false;
							
							for(int k=0; k<24; k++) {
								// opponent checker
								if((board[opponentPlayer] & (1 << k)) != 0 && !BitBoardUtils.hasCompletedMorris(board, k, opponentPlayer)) {
									remove = k;
									temp = new BitBoardAction(from, to, remove, playerToMove);
									result.add(temp);
									foundRemovableChecker = true;
								}
							}
							
							if(!foundRemovableChecker) {
								for(int k=0; k<24; k++) {
									// opponent checker
									if((board[opponentPlayer] & (1 << k)) != 0 && !BitBoardUtils.hasCompletedMorris(board, k, opponentPlayer)) {
										remove = k;
										temp = new BitBoardAction(from, to, remove, playerToMove);
										result.add(temp);
									}
								}
							}							
						} else {
							temp = new BitBoardAction(from, to, -1, playerToMove);
							result.add(temp);
						}
					}
				}
			}
			/*
			 * resetto from, to e remove?
			 */
		}
		
		return result;
	}

}
