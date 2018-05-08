package it.unibo.ai.mulino.CIRAMill.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.mulino.CIRAMill.minimax.IAction;
import it.unibo.ai.mulino.CIRAMill.minimax.IState;

public class BitBoardState implements IState {

	public static final byte DEFAULT_INITIAL_CHECKERS = 9;

	public static final byte WHITE = 0;
	public static final byte BLACK = 1;

	public static final byte MIDGAME = 0;

	private static final byte A7 = 0;
	private static final byte D7 = 1;
	private static final byte G7 = 2;
	private static final byte G4 = 3;
	private static final byte G1 = 4;
	private static final byte D1 = 5;
	private static final byte A1 = 6;
	private static final byte A4 = 7;

	private static final byte B6 = 8;
	private static final byte D6 = 9;
	private static final byte F6 = 10;
	private static final byte F4 = 11;
	private static final byte F2 = 12;
	private static final byte D2 = 13;
	private static final byte B2 = 14;
	private static final byte B4 = 15;

	private static final byte C5 = 16;
	private static final byte D5 = 17;
	private static final byte E5 = 18;
	private static final byte E4 = 19;
	private static final byte E3 = 20;
	private static final byte D3 = 21;
	private static final byte C3 = 22;
	private static final byte C4 = 23;

	private static final int MILL_0_1_2 = (1 << A7) | (1 << D7) | (1 << G7);
	private static final int MILL_2_3_4 = (1 << G7) | (1 << G4) | (1 << G1);
	private static final int MILL_4_5_6 = (1 << G1) | (1 << D1) | (1 << A1);
	private static final int MILL_6_7_0 = (1 << A1) | (1 << A4) | (1 << A7);

	private static final int MILL_8_9_10 = (1 << B6) | (1 << D6) | (1 << F6);
	private static final int MILL_10_11_12 = (1 << F6) | (1 << F4) | (1 << F2);
	private static final int MILL_12_13_14 = (1 << F2) | (1 << D2) | (1 << B2);
	private static final int MILL_14_15_8 = (1 << B2) | (1 << B4) | (1 << B6);

	private static final int MILL_16_17_18 = (1 << C5) | (1 << D5) | (1 << E5);
	private static final int MILL_18_19_20 = (1 << E5) | (1 << E4) | (1 << E3);
	private static final int MILL_20_21_22 = (1 << E3) | (1 << D3) | (1 << C3);
	private static final int MILL_22_23_16 = (1 << C3) | (1 << C4) | (1 << C5);

	private static final int MILL_1_9_17 = (1 << D7) | (1 << D6) | (1 << D5);
	private static final int MILL_3_11_19 = (1 << G4) | (1 << F4) | (1 << E4);
	private static final int MILL_5_13_21 = (1 << D1) | (1 << D2) | (1 << D3);
	private static final int MILL_7_15_23 = (1 << A4) | (1 << B4) | (1 << C4);

	private static final int[] MILLS = { MILL_0_1_2, MILL_2_3_4, MILL_4_5_6, MILL_6_7_0, MILL_8_9_10, MILL_10_11_12,
			MILL_12_13_14, MILL_14_15_8, MILL_16_17_18, MILL_18_19_20, MILL_20_21_22, MILL_22_23_16, MILL_1_9_17,
			MILL_3_11_19, MILL_5_13_21, MILL_7_15_23 };

	private static final int[][] POSITION_MILLS = {
			{MILL_0_1_2, MILL_6_7_0},
			{MILL_0_1_2, MILL_1_9_17},
			{MILL_0_1_2, MILL_2_3_4},
			{MILL_2_3_4, MILL_3_11_19},
			{MILL_2_3_4, MILL_4_5_6},
			{MILL_4_5_6, MILL_5_13_21},
			{MILL_4_5_6, MILL_6_7_0},
			{MILL_6_7_0, MILL_7_15_23},
			
			{MILL_8_9_10, MILL_14_15_8},
			{MILL_8_9_10, MILL_1_9_17},
			{MILL_8_9_10, MILL_10_11_12},
			{MILL_10_11_12, MILL_3_11_19},
			{MILL_10_11_12, MILL_12_13_14},
			{MILL_12_13_14, MILL_5_13_21},
			{MILL_12_13_14, MILL_14_15_8},
			{MILL_14_15_8, MILL_7_15_23},
			
			{MILL_16_17_18, MILL_22_23_16},
			{MILL_16_17_18, MILL_1_9_17},
			{MILL_16_17_18, MILL_18_19_20},
			{MILL_18_19_20, MILL_3_11_19},
			{MILL_18_19_20, MILL_20_21_22},
			{MILL_20_21_22, MILL_5_13_21},
			{MILL_20_21_22, MILL_22_23_16},
			{MILL_22_23_16, MILL_7_15_23}
	};

	private static final int[][] ADJACENT_POSITIONS = {
			{D7, A4},
			{A7, G7, D6},
			{D7, G4},
			{G7, G1, F4},
			{G4, D1},
			{G1, A1, D2},
			{D1, A4},
			{A1, A7, B4},
			
			{D6, B4},
			{D7, B6, F6, D5},
			{D6, F4},
			{G4, F6, F2, E4},
			{F4, D2},
			{D1, F2, B2, D3},
			{D2, B4},
			{A4, B6, B2, C4},
			
			{D5, C4},
			{D6, C5, E5},
			{D5, E4},
			{F4, E5, E3},
			{E4, D3},
			{D2, E3, C3},
			{D3, C4},
			{B4, C3, C5}			
	};

//	private static final int INITIALPHASE_HAS_COMPLETED_MORRIS = 18;
	private static final int INITIALPHASE_CLOSED_MORRIS = 26;
	private static final int INITIALPHASE_BLOCKED_PIECES = 1;
	private static final int INITIALPHASE_TWO_PIECES_CONFIGURATION = 12;
	private static final int INITIALPHASE_THREE_PIECES_CONFIGURATION = 7;
	private static final int INITIALPHASE_PIECES_NUMBER = 6;
	
//	private static final int MIDGAME_HAS_COMPLETED_MORRIS = 14;
	private static final int MIDGAME_CLOSED_MORRIS = 43;
	private static final int MIDGAME_BLOCKED_PIECES = 10;
	private static final int MIDGAME_DOUBLE_MORRIS = 42;
	private static final int MIDGAME_PIECES_NUMBER = 6;
	private static final int MIDGAME_OPENED_MORRIS = 7;
	
	private static final int ENDGAME_TWO_PIECES_CONFIGURATION = 10;
	private static final int ENDGAME_THREE_PIECES_CONFIGURATION = 1;
//	private static final int ENDGAME_HAS_COMPLETED_MORRIS = 16;
	private static final int ENDGAME_CLOSED_MORRIS = 16;
	
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

		gamePhase = ~MIDGAME;
	}

	public BitBoardState(int initialWhiteCheckers, int initialBlackCheckers) {
		this((byte) checkIntToByte(initialWhiteCheckers), (byte) checkIntToByte(initialBlackCheckers));
	}

	public BitBoardState(int whiteCheckersToPut, int blackCheckersToPut, int whiteBitBoard, int blackBitBoard, byte playerToMove) {
		this();
		
		checkersToPut[WHITE] = (byte) whiteCheckersToPut;
		checkersToPut[BLACK] = (byte) blackCheckersToPut;

		board[WHITE] = whiteBitBoard;
		board[BLACK] = blackBitBoard;
		
		checkersOnBoard[WHITE] = (byte) Integer.bitCount(board[WHITE]);
		checkersOnBoard[BLACK] = (byte) Integer.bitCount(board[BLACK]);
		
		gamePhase = (byte) (checkersToPut[WHITE] | checkersToPut[BLACK]);
		
		if(playerToMove != WHITE && playerToMove != BLACK)
			throw new IllegalArgumentException("Player must be white or black");
		
		this.playerToMove = playerToMove;
	}

	public BitBoardState() {
		this(DEFAULT_INITIAL_CHECKERS, DEFAULT_INITIAL_CHECKERS);
	}

	public static int checkIntToByte(int value) {
		if (value > Byte.MAX_VALUE)
			throw new IllegalArgumentException("Initial checkers must be less or equal to  " + Byte.MAX_VALUE);
		return value;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("7 ");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("a7")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("a7")) != 0 ? "B" : "O");
		result.append("--------");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("d7")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("d7")) != 0 ? "B" : "O");
		result.append("--------");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("g7")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("g7")) != 0 ? "B" : "O");
		result.append("\n");

		result.append("6 |--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("b6")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("b6")) != 0 ? "B" : "O");
		result.append("-----");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("d6")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("d6")) != 0 ? "B" : "O");
		result.append("-----");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("f6")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("f6")) != 0 ? "B" : "O");
		result.append("--|\n");

		result.append("5 |--|--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("c5")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("c5")) != 0 ? "B" : "O");
		result.append("--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("d5")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("d5")) != 0 ? "B" : "O");
		result.append("--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("e5")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("e5")) != 0 ? "B" : "O");
		result.append("--|--|\n");

		result.append("4 ");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("a4")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("a4")) != 0 ? "B" : "O");
		result.append("--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("b4")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("b4")) != 0 ? "B" : "O");
		result.append("--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("c4")) != 0 ? "W" : (board[BLACK] & BitBoardUtils.boardFromPosition("c4")) != 0 ? "B" : "O");
		result.append("     ");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("e4")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("e4")) != 0 ? "B" : "O");
		result.append("--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("f4")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("f4")) != 0 ? "B" : "O");
		result.append("--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("g4")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("g4")) != 0 ? "B" : "O");
		result.append("\n");

		result.append("3 |--|--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("c3")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("c3")) != 0 ? "B" : "O");
		result.append("--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("d3")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("d3")) != 0 ? "B" : "O");
		result.append("--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("e3")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("e3")) != 0 ? "B" : "O");
		result.append("--|--|\n");

		result.append("2 |--");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("b2")) != 0 ? "W" : (board[BLACK] & BitBoardUtils.boardFromPosition("b2")) != 0 ? "B" : "O");
		result.append("-----");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("d2")) != 0 ? "W"	: (board[BLACK] & BitBoardUtils.boardFromPosition("d2")) != 0 ? "B" : "O");
		result.append("-----");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("f2")) != 0 ? "W" : (board[BLACK] & BitBoardUtils.boardFromPosition("f2")) != 0 ? "B" : "O");
		result.append("--|\n");

		result.append("1 ");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("a1")) != 0 ? "W" : (board[BLACK] & BitBoardUtils.boardFromPosition("a1")) != 0 ? "B" : "O");
		result.append("--------");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("d1")) != 0 ? "W" : (board[BLACK] & BitBoardUtils.boardFromPosition("d1")) != 0 ? "B" : "O");
		result.append("--------");
		result.append((board[WHITE] & BitBoardUtils.boardFromPosition("g1")) != 0 ? "W" : (board[BLACK] & BitBoardUtils.boardFromPosition("g1")) != 0 ? "B" : "O");
		result.append("\n");

		result.append("  a  b  c  d  e  f  g\n");
		result.append("Player: " + (playerToMove == WHITE ? "WHITE" : "BLACK") + ";\n");
		result.append("Phase: " + (gamePhase == MIDGAME ? "MIDGAME" : "INITIALPHASE") + ";\n");
		result.append("White Checkers: " + checkersToPut[WHITE] + ";\n");
		result.append("Black Checkers: " + checkersToPut[BLACK] + ";\n");
		result.append("White Checkers On Board: " + checkersOnBoard[WHITE] + ";\n");
		result.append("Black Checkers On Board: " + checkersOnBoard[BLACK] + ";\n");

		return result.toString();
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
		case FIRST:
			result.gamePhase = ~MIDGAME;
			break;
		case SECOND:
			result.gamePhase = BitBoardState.MIDGAME;
			break;
		case FINAL:
			result.gamePhase = BitBoardState.MIDGAME;
			break;
		default:
			throw new Exception("Wrong Phase");
		}

		return result;
	}

	/* 
	 * Calcolando le mosse possibili si hanno in automatico anche i successori in
	 * quanto, data un azione, lo stato successivo e' dato da:
	 * 
	 * board[player] 	^= action.getFrom 
	 * board[player]	|= action.getTo 
	 * board[opponent]	^= action.getRemove
	 */

	@Override
	public List<IAction> getFollowingMoves() {
		List<IAction> result = new ArrayList<>();

		if(gamePhase == MIDGAME) {
			if (checkersOnBoard[playerToMove] > 3) {
				result = getFollowingMovesMidGame();
			} else {
				result = getFollowingMovesEndGame();
			}
		} else {
			result = getFollowingMovesInitialPhase();
		}
		
		return result;
	}

	private List<IAction> getFollowingMovesInitialPhase() {
		List<IAction> result = new ArrayList<>();
		BitBoardAction temp;
		int to;
		int remove;
		byte opponentPlayer = playerToMove == BitBoardState.WHITE ? BitBoardState.BLACK : BitBoardState.WHITE;

		for (int i = 0; i < 24; i++) {
			to = 1 << i;
			
			// empty position
			if (((board[WHITE] | board[BLACK]) & to) == 0) {				

				if (hasCompletedMorris(0, i, playerToMove)) {
					boolean foundRemovableChecker = false;

					for (int j = 0; j < 24; j++) {
						remove = 1 << j;
						
						// opponent checker
						if ((board[opponentPlayer] & remove) != 0 && !hasCompletedMorris(0, j, opponentPlayer)) {
							temp = new BitBoardAction(0, to, remove);
							result.add(temp);
							foundRemovableChecker = true;
						}
					}
					if (!foundRemovableChecker) {
						for (int j = 0; j < 24; j++) {
							remove = 1 << j;
							
							// opponent checker
							if ((board[opponentPlayer] & remove) != 0 && hasCompletedMorris(0, j, opponentPlayer)) {								
								temp = new BitBoardAction(0, to, remove);
								result.add(temp);
							}
						}
					}
				} else {
					temp = new BitBoardAction(0, to, 0);
					result.add(temp);
				}
			}
		}

		return result;
	}

	private List<IAction> getFollowingMovesMidGame() {
		List<IAction> result = new ArrayList<>();
		BitBoardAction temp;
		int from;
		int to;
		int remove;
		byte opponentPlayer = playerToMove == BitBoardState.WHITE ? BitBoardState.BLACK : BitBoardState.WHITE;

		for (int i = 0; i < 24; i++) {
			from = 1 << i;
			
			// player checker
			if ((board[playerToMove] & from) != 0) {				

				for (Integer adjacentPosition : ADJACENT_POSITIONS[i]) {
					to = 1 << adjacentPosition;
					
					// empty pos
					if (((board[WHITE] | board[BLACK]) & to) == 0) {

						if (hasCompletedMorris(from, adjacentPosition, playerToMove)) {
							boolean foundRemovableChecker = false;

							for (int j = 0; j < 24; j++) {
								remove = 1 << j;
								
								// opponent checker
								if ((board[opponentPlayer] & remove) != 0 && !hasCompletedMorris(0, j, opponentPlayer)) {
									temp = new BitBoardAction(from, to, remove);
									result.add(temp);
									foundRemovableChecker = true;
								}
							}

							if (!foundRemovableChecker) {
								for (int j = 0; j < 24; j++) {
									remove = 1 << j;
									
									// opponent checker
									if ((board[opponentPlayer] & remove) != 0 && hasCompletedMorris(0, j, opponentPlayer)) {
										temp = new BitBoardAction(from, to, remove);
										result.add(temp);
									}
								}
							}
						} else {
							temp = new BitBoardAction(from, to, 0);
							result.add(temp);
						}
					}
				}
			}
		}

		return result;
	}

	private List<IAction> getFollowingMovesEndGame() {
		List<IAction> result = new ArrayList<>();
		BitBoardAction temp = new BitBoardAction();
		int from;
		int to;
		int remove;
		byte opponentPlayer = playerToMove == BitBoardState.WHITE ? BitBoardState.BLACK : BitBoardState.WHITE;

		for (int i = 0; i < 24; i++) {
			from = 1 << i;
			
			// player checker
			if ((board[playerToMove] & from) != 0) {

				for (int j = 0; j < 24; j++) {
					to = 1 << j;
					
					// empty position
					if (((board[WHITE] | board[BLACK]) & to) == 0) {

						if (hasCompletedMorris(from, j, playerToMove)) {
							boolean foundRemovableChecker = false;

							for (int k = 0; k < 24; k++) {
								remove = 1 << k;
								
								// opponent checker
								if ((board[opponentPlayer] & remove) != 0 && !hasCompletedMorris(0, k, opponentPlayer)) {									
									temp = new BitBoardAction(from, to, remove);
									result.add(temp);
									foundRemovableChecker = true;
								}
							}

							if (!foundRemovableChecker) {
								for (int k = 0; k < 24; k++) {
									remove = 1 << k;
									
									// opponent checker
									if ((board[opponentPlayer] & remove) != 0 && !hasCompletedMorris(0, k, opponentPlayer)) {
										temp = new BitBoardAction(from, to, remove);
										result.add(temp);
									}
								}
							}
						} else {
							temp = new BitBoardAction(from, to, 0);
							result.add(temp);
						}
					}
				}
			}
		}

		return result;
	}

	public boolean hasCompletedMorris(int bitFrom, int intTo, byte player) {
		int tempBoard = (board[player] | (1 << intTo)) ^ bitFrom;
		
		for(Integer mill : POSITION_MILLS[intTo]) {
			if((tempBoard & mill) == mill)
				return true;
		}
		
		return false;
	}

	@Override
	public void move(IAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unmove(IAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IState applyMove(IAction action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IState clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHeuristicEvaluation() {
		int result;
		
		if(gamePhase == MIDGAME) {
			if (checkersOnBoard[playerToMove] > 3) {
				result = getHeuristicEvaluationMidGame();
			} else {
				result = getHeuristicEvaluationEndGame();
			}
		} else {
			result = getHeuristicEvaluationInitialPhase();
		}
		
		return result;
	}
	
	private int getHeuristicEvaluationInitialPhase() {
		int result = 0;
		byte opponentPlayer = playerToMove == WHITE ? BLACK : WHITE;
		
		//HAS_COMPLETED_MORRIS
		
		//CLOSED_MORRIS
		int closedMorrisPlayer = 0;
		int closedMorrisOpponent = 0;
		
		//TWO_PIECES_CONFIGURATION
		int twoPiecesConfigurationPlayer = 0;
		int twoPiecesConfigurationOpponent = 0;
		
		for(int mill : MILLS) {
			//CLOSED_MORRIS
			if((board[playerToMove] & mill) == mill) {
				closedMorrisPlayer++;
			} else if((board[opponentPlayer] & mill) == mill) {
				closedMorrisOpponent++;
			}
			
			//TWO_PIECES_CONFIGURATION
			if((board[opponentPlayer] & mill) == 0 && Integer.bitCount(board[playerToMove] & mill) == 2) {
				twoPiecesConfigurationPlayer++;
			} else if((board[playerToMove] & mill) == 0 && Integer.bitCount(board[opponentPlayer] & mill) == 2) {
				twoPiecesConfigurationOpponent++;
			}
		}
		
		//BLOCKED_PIECES
		int blockedPiecesPlayer = 0;
		int blockedPiecesOpponent = 0;
		
		//THREE_PIECES_CONFIGURATION
		int threePiecesConfigurationPlayer = 0;
		int threePiecesConfigurationOpponent = 0;
		
		int temp;
		
		for(int i=0; i<24; i++) {
			temp = 1 << i;
			
			//BLOCKED_PIECES
			if((board[opponentPlayer] & temp) != 0) {
				boolean isBlocked = true;
				
				for(int adjacentPosition : ADJACENT_POSITIONS[i]) {
					if(((board[playerToMove] | board[opponentPlayer]) & (1 << adjacentPosition)) == 0) {
						isBlocked = false;
						break;
					}
				}
				if(isBlocked)
					blockedPiecesPlayer++;
			}
			
			if((board[playerToMove] & temp) != 0) {
				boolean isBlocked = true;
				
				for(int adjacentPosition : ADJACENT_POSITIONS[i]) {
					if(((board[playerToMove] | board[opponentPlayer]) & (1 << adjacentPosition)) == 0) {
						isBlocked = false;
						break;
					}
				}
				if(isBlocked)
					blockedPiecesOpponent++;
			}
			
			//THREE_PIECES_CONFIGURATION
			if((board[playerToMove] & temp) != 0) {
				boolean check = true;
				for(int mill : POSITION_MILLS[i]) {
					if(!((board[opponentPlayer] & mill) == 0 && Integer.bitCount(board[playerToMove] & mill) == 2)) {
						check = false;
						break;
					}
				}
				if(check) {
					threePiecesConfigurationPlayer++;
				}
			}
			
			if((board[opponentPlayer] & temp) != 0) {
				boolean check = true;
				for(int mill : POSITION_MILLS[i]) {
					if(!((board[playerToMove] & mill) == 0 && Integer.bitCount(board[opponentPlayer] & mill) == 2)) {
						check = false;
						break;
					}
				}
				if(check) {
					threePiecesConfigurationOpponent++;
				}
			}
			
		}
		
		result += INITIALPHASE_CLOSED_MORRIS * closedMorrisPlayer;
		result -= INITIALPHASE_CLOSED_MORRIS * closedMorrisOpponent;
		
		result += INITIALPHASE_BLOCKED_PIECES * blockedPiecesPlayer;
		result -= INITIALPHASE_BLOCKED_PIECES * blockedPiecesOpponent;
		
		result += INITIALPHASE_TWO_PIECES_CONFIGURATION * twoPiecesConfigurationPlayer;
		result -= INITIALPHASE_TWO_PIECES_CONFIGURATION * twoPiecesConfigurationOpponent;
		
		result += INITIALPHASE_THREE_PIECES_CONFIGURATION * threePiecesConfigurationPlayer;
		result -= INITIALPHASE_THREE_PIECES_CONFIGURATION * threePiecesConfigurationOpponent;
		
		result += INITIALPHASE_PIECES_NUMBER * (checkersOnBoard[playerToMove] - checkersOnBoard[opponentPlayer]);
		
		return result;
	}
	
	private int getHeuristicEvaluationMidGame() {
		int result = 0;
		byte opponentPlayer = playerToMove == WHITE ? BLACK : WHITE;
		
		//CLOSED_MORRIS
		int closedMorrisPlayer = 0;
		int closedMorrisOpponent = 0;
		
		//OPENED_MORRIS
		int openedMorrisPlayer = 0;
		int openedMorrisOpponent = 0;
		
		for(int mill : MILLS) {
			//CLOSED_MORRIS
			if((board[playerToMove] & mill) == mill) {
				closedMorrisPlayer++;
			} else if((board[opponentPlayer] & mill) == mill) {
				closedMorrisOpponent++;
			}
			
			//OPENED_MORRIS
			// si potrebbe controllare che l'avversario non sia adiacente alla posizione libera
			if((board[opponentPlayer] & mill) == 0 && Integer.bitCount(board[playerToMove] & mill) == 2) {
				for(int adjacentPosition : ADJACENT_POSITIONS[Integer.numberOfTrailingZeros((board[playerToMove] & mill) ^ mill)]) {
					if((board[playerToMove] & adjacentPosition) != 0) {
						openedMorrisPlayer++;
						break;
					}
				}
			} else if((board[playerToMove] & mill) == 0 && Integer.bitCount(board[opponentPlayer] & mill) == 2) {
				for(int adjacentPosition : ADJACENT_POSITIONS[Integer.numberOfTrailingZeros((board[opponentPlayer] & mill) ^ mill)]) {
					if((board[opponentPlayer] & adjacentPosition) != 0) {
						openedMorrisOpponent++;
						break;
					}
				}
			}
		}
		
		//DOUBLE_MORRIS
		int doubleMorrisPlayer = 0;
		int doubleMorrisOpponent = 0;
		
		//BLOCKED_PIECES
		int blockedPiecesPlayer = 0;
		int blockedPiecesOpponent = 0;
		
		int temp;
		
		for(int i=0; i<24; i++) {
			temp = 1 << i;
			
			//DOUBLE_MORRIS
			boolean check = true;
			for(int mill : POSITION_MILLS[i]) {
				if((board[playerToMove] & mill) != mill) {
					check = false;
					break;
				}
			}
			if(check) {
				doubleMorrisPlayer++;
			}
			
			check = true;
			for(int mill : POSITION_MILLS[i]) {
				if((board[opponentPlayer] & mill) != mill) {
					check = false;
					break;
				}
			}
			if(check) {
				doubleMorrisOpponent++;
			}
			
			//BLOCKED_PIECES
			if((board[opponentPlayer] & temp) != 0) {
				boolean isBlocked = true;
				
				for(int adjacentPosition : ADJACENT_POSITIONS[i]) {
					if(((board[playerToMove] | board[opponentPlayer]) & (1 << adjacentPosition)) == 0) {
						isBlocked = false;
						break;
					}
				}
				if(isBlocked)
					blockedPiecesPlayer++;
			}
			
			if((board[playerToMove] & temp) != 0) {
				boolean isBlocked = true;
				
				for(int adjacentPosition : ADJACENT_POSITIONS[i]) {
					if(((board[playerToMove] | board[opponentPlayer]) & (1 << adjacentPosition)) == 0) {
						isBlocked = false;
						break;
					}
				}
				if(isBlocked)
					blockedPiecesOpponent++;
			}
		}
		
		result += MIDGAME_CLOSED_MORRIS * closedMorrisPlayer;
		result -= MIDGAME_CLOSED_MORRIS * closedMorrisOpponent;
		
		result += MIDGAME_OPENED_MORRIS * openedMorrisPlayer;
		result -= MIDGAME_OPENED_MORRIS * openedMorrisOpponent;
		
		result += MIDGAME_DOUBLE_MORRIS * doubleMorrisPlayer;
		result -= MIDGAME_DOUBLE_MORRIS * doubleMorrisOpponent;
		
		result += MIDGAME_BLOCKED_PIECES * blockedPiecesPlayer;
		result -= MIDGAME_BLOCKED_PIECES * blockedPiecesOpponent;
		
		result += MIDGAME_PIECES_NUMBER * (checkersOnBoard[playerToMove] - checkersOnBoard[opponentPlayer]);
		
		return result;
	}
	
	private int getHeuristicEvaluationEndGame() {
		int result = 0;
		byte opponentPlayer = playerToMove == WHITE ? BLACK : WHITE;
		
		//CLOSED_MORRIS
		int closedMorrisPlayer = 0;
		int closedMorrisOpponent = 0;
		
		//TWO_PIECES_CONFIGURATION
		int twoPiecesConfigurationPlayer = 0;
		int twoPiecesConfigurationOpponent = 0;
		
		for(int mill : MILLS) {
			//CLOSED_MORRIS
			if((board[playerToMove] & mill) == mill) {
				closedMorrisPlayer++;
			} else if((board[opponentPlayer] & mill) == mill) {
				closedMorrisOpponent++;
			}
			
			//TWO_PIECES_CONFIGURATION
			if((board[opponentPlayer] & mill) == 0 && Integer.bitCount(board[playerToMove] & mill) == 2) {
				twoPiecesConfigurationPlayer++;
			} else if((board[playerToMove] & mill) == 0 && Integer.bitCount(board[opponentPlayer] & mill) == 2) {
				twoPiecesConfigurationOpponent++;
			}
		}

		//THREE_PIECES_CONFIGURATION
		int threePiecesConfigurationPlayer = 0;
		int threePiecesConfigurationOpponent = 0;
		
		int temp;
		
		for(int i=0; i<24; i++) {
			temp = 1 << i;
			
			//THREE_PIECES_CONFIGURATION
			if((board[playerToMove] & temp) != 0) {
				boolean check = true;
				for(int mill : POSITION_MILLS[i]) {
					if(!((board[opponentPlayer] & mill) == 0 && Integer.bitCount(board[playerToMove] & mill) == 2)) {
						check = false;
						break;
					}
				}
				if(check) {
					threePiecesConfigurationPlayer++;
				}
			}


			if((board[opponentPlayer] & temp) != 0) {
				boolean check = true;
				for(int mill : POSITION_MILLS[i]) {
					if(!((board[playerToMove] & mill) == 0 && Integer.bitCount(board[opponentPlayer] & mill) == 2)) {
						check = false;
						break;
					}
				}
				if(check) {
					threePiecesConfigurationOpponent++;
				}
			}

		}
		
		result += ENDGAME_CLOSED_MORRIS * closedMorrisPlayer;
		result -= ENDGAME_CLOSED_MORRIS * closedMorrisOpponent;
		
		result += ENDGAME_TWO_PIECES_CONFIGURATION * twoPiecesConfigurationPlayer;
		result -= ENDGAME_TWO_PIECES_CONFIGURATION * twoPiecesConfigurationOpponent;
		
		result += ENDGAME_THREE_PIECES_CONFIGURATION * threePiecesConfigurationPlayer;
		result -= ENDGAME_THREE_PIECES_CONFIGURATION * threePiecesConfigurationOpponent;
		
		return result;
	}
	
	@Override
	public boolean isWinningState() {
		if(gamePhase != MIDGAME)
			return false;
		if(playerToMove == WHITE && checkersOnBoard[BLACK] < 3)
			return true;
		else if(playerToMove == BLACK && checkersOnBoard[WHITE] < 3)
			return true;

		byte opponentPlayer = playerToMove == WHITE ? BLACK : WHITE;
		
		if(checkersOnBoard[opponentPlayer] > 3) {
			
			for(int i=0; i<24; i++) {				
				if((board[opponentPlayer] & (1 << i)) != 0) {
//					boolean isBlocked = true;
					
					for(int position : ADJACENT_POSITIONS[i]) {
						if(((board[WHITE] | board[BLACK]) & (1 << position)) == 0) {
//							isBlocked = false;
//							break;
							return false;
						}
					}
					
//					if(!isBlocked)
//						return false;
				}
			}
			return true;
		}
		
		return false;
	}
	
}
