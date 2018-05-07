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

	private static int[] MILLS = { MILL_0_1_2, MILL_2_3_4, MILL_4_5_6, MILL_6_7_0, MILL_8_9_10, MILL_10_11_12,
			MILL_12_13_14, MILL_14_15_8, MILL_16_17_18, MILL_18_19_20, MILL_20_21_22, MILL_22_23_16, MILL_1_9_17,
			MILL_3_11_19, MILL_5_13_21, MILL_7_15_23 };

	private static int[][] POSITION_MILLS = {
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

	private static int[][] ADJACENT_POSITIONS = {
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

	public BitBoardState(int initialWhiteCheckers, int initialBlackCheckers, int whiteBitBoard, int blackBitBoard) {
		this((byte) checkIntToByte(initialWhiteCheckers), (byte) checkIntToByte(initialBlackCheckers));

		board[WHITE] = whiteBitBoard;
		board[BLACK] = blackBitBoard;
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
		StringBuffer result = new StringBuffer();
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
		result.append("Phase: " + (gamePhase == 10 ? "INITIALPHASE" : "MIDGAME") + ";\n");
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
			result.gamePhase = INITIALPHASE;
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
	 * SECONDO ME: (dovrebbe essere giusto anyway)
	 * 
	 * calcolando le mosse possibili si hanno in automatico anche i successori in
	 * quanto, data un azione, lo stato successivo è dato da:
	 * 
	 * newState ^= action.getFrom newState |= action.getTo newState ^=
	 * action.getRemove
	 */

	public ArrayList<BitBoardAction> getFollowingMoves() throws Exception {
		ArrayList<BitBoardAction> result = new ArrayList<>();

		switch (gamePhase) {
		case INITIALPHASE:
			result = getFollowingMovesInitialPhase();
			break;
		case MIDGAME:

			// TODO
			
			if (checkersOnBoard[playerToMove] > 3) {
				result = getFollowingMovesMidGame();
			} else {
				result = getFollowingMovesEndGame();
			}
			break;
		default:
			throw new Exception("Wrong Phase");
		}

		return result;
	}

	private ArrayList<BitBoardAction> getFollowingMovesInitialPhase() {
		ArrayList<BitBoardAction> result = new ArrayList<>();
		BitBoardAction temp;
		int to;
		int remove;
		byte opponentPlayer = playerToMove == BitBoardState.WHITE ? BitBoardState.BLACK : BitBoardState.WHITE;

		for (int i = 0; i < 24; i++) {
			to = 1 << i;
			
			// empty position
			if (((board[WHITE] | board[BLACK]) & to) == 0) {				

				if (hasCompletedMorris(i, playerToMove)) {
					boolean foundRemovableChecker = false;

					for (int j = 0; j < 24; j++) {
						remove = 1 << j;
						
						// opponent checker
						if ((board[opponentPlayer] & remove) != 0 && !hasCompletedMorris(j, opponentPlayer)) {
							temp = new BitBoardAction(0, to, remove, playerToMove);
							result.add(temp);
							foundRemovableChecker = true;
						}
					}
					if (!foundRemovableChecker) {
						for (int j = 0; j < 24; j++) {
							remove = 1 << j;
							
							// opponent checker
							if ((board[opponentPlayer] & remove) != 0 && hasCompletedMorris(j, opponentPlayer)) {								
								temp = new BitBoardAction(0, to, remove, playerToMove);
								result.add(temp);
							}
						}
					}
				} else {
					temp = new BitBoardAction(0, to, 0, playerToMove);
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
		BitBoardAction temp;
		int from;
		int to;
		int remove;
		byte opponentPlayer = playerToMove == BitBoardState.WHITE ? BitBoardState.BLACK : BitBoardState.WHITE;

		for (int i = 0; i < 24; i++) {
			from = 1 << i;
			
			// player checker
			if ((board[playerToMove] & from) != 0) {				

				for (Integer adjacentPosition : getAdjacentPositions(i)) {
					to = 1 << adjacentPosition;
					
					// empty pos
					if (((board[WHITE] | board[BLACK]) & to) == 0) {

						if (hasCompletedMorris(adjacentPosition, playerToMove)) {
							boolean foundRemovableChecker = false;

							for (int j = 0; j < 24; j++) {
								remove = 1 << j;
								
								// opponent checker
								if ((board[opponentPlayer] & remove) != 0 && !hasCompletedMorris(j, opponentPlayer)) {
									temp = new BitBoardAction(from, to, remove, playerToMove);
									result.add(temp);
									foundRemovableChecker = true;
								}
							}

							if (!foundRemovableChecker) {
								for (int j = 0; j < 24; j++) {
									remove = 1 << j;
									
									// opponent checker
									if ((board[opponentPlayer] & remove) != 0 && hasCompletedMorris(j, opponentPlayer)) {
										temp = new BitBoardAction(from, to, remove, playerToMove);
										result.add(temp);
									}
								}
							}
						} else {
							temp = new BitBoardAction(from, to, 0, playerToMove);
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

		for (int i = 0; i < 24; i++) {
			from = 1 << i;
			
			// player checker
			if ((board[playerToMove] & from) != 0) {

				for (int j = 0; j < 24; j++) {
					to = 1 << j;
					
					// empty position
					if (((board[WHITE] | board[BLACK]) & to) == 0) {

						if (hasCompletedMorris(j, playerToMove)) {
							boolean foundRemovableChecker = false;

							for (int k = 0; k < 24; k++) {
								remove = 1 << k;
								
								// opponent checker
								if ((board[opponentPlayer] & remove) != 0 && !hasCompletedMorris(k, opponentPlayer)) {									
									temp = new BitBoardAction(from, to, remove, playerToMove);
									result.add(temp);
									foundRemovableChecker = true;
								}
							}

							if (!foundRemovableChecker) {
								for (int k = 0; k < 24; k++) {
									remove = 1 << k;
									
									// opponent checker
									if ((board[opponentPlayer] & remove) != 0 && !hasCompletedMorris(k, opponentPlayer)) {
										temp = new BitBoardAction(from, to, remove, playerToMove);
										result.add(temp);
									}
								}
							}
						} else {
							temp = new BitBoardAction(from, to, 0, playerToMove);
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

	// le posizioni sono in base 10

	// TODO
	public boolean hasCompletedMorris(int position, byte player) {
		int tempBoard = board[player] | (1 << position);
		
		for(Integer mill : POSITION_MILLS[position]) {
			if((tempBoard & mill) == mill)
				return true;
		}
		
		return false;
	}

	// TODO
	public static int[] getAdjacentPositions(int position) {
		return ADJACENT_POSITIONS[position];
	}

}
