package it.unibo.ai.didattica.mulino.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.FromAndToAreEqualsException;
import it.unibo.ai.didattica.mulino.actions.FromAndToAreNotConnectedException;
import it.unibo.ai.didattica.mulino.actions.NoMoreCheckersAvailableException;
import it.unibo.ai.didattica.mulino.actions.NullActionException;
import it.unibo.ai.didattica.mulino.actions.NullCheckerException;
import it.unibo.ai.didattica.mulino.actions.NullStateException;
import it.unibo.ai.didattica.mulino.actions.Phase1;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Phase2;
import it.unibo.ai.didattica.mulino.actions.Phase2Action;
import it.unibo.ai.didattica.mulino.actions.PhaseFinal;
import it.unibo.ai.didattica.mulino.actions.PhaseFinalAction;
import it.unibo.ai.didattica.mulino.actions.PositionNotEmptyException;
import it.unibo.ai.didattica.mulino.actions.TryingToMoveOpponentCheckerException;
import it.unibo.ai.didattica.mulino.actions.TryingToRemoveCheckerInTripleException;
import it.unibo.ai.didattica.mulino.actions.TryingToRemoveEmptyCheckerException;
import it.unibo.ai.didattica.mulino.actions.TryingToRemoveOwnCheckerException;
import it.unibo.ai.didattica.mulino.actions.Util;
import it.unibo.ai.didattica.mulino.actions.WrongPhaseException;
import it.unibo.ai.didattica.mulino.actions.WrongPositionException;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;
import it.unibo.ai.didattica.mulino.domain.ValuedAction;

public class MulinoClientFirstMiniMax extends MulinoClient {

	public MulinoClientFirstMiniMax(Checker player) throws UnknownHostException, IOException {
		super(player);
	}
	
	public static Checker player;
	private static int expandedStates;
	private static long elapsedTime;
	public static final int MAXDEPTH = 1;
//	public static final int THREAD_NUMBER = 4;
	public static void main(String[] args) throws Exception {
		MulinoClient mulinoClient = new MulinoClientFirstMiniMax(Checker.WHITE);
		player = mulinoClient.getPlayer();
		State currentState = null;
		
		
		do {
			expandedStates = 0;
			
			if (player == Checker.WHITE) {
				currentState = mulinoClient.read();
				System.out.println("Current state is:");
				System.out.println(currentState.toString());
				
				Action a = minimaxDecision(currentState, MAXDEPTH);
				
				mulinoClient.write(a);
				
				currentState = mulinoClient.read();
				System.out.println("New state is:");
				System.out.println(currentState.toString());
				
				System.out.println("\nWaiting for opponent move...");
				
				
			} else if (player == Checker.BLACK) {
				currentState = mulinoClient.read();
				System.out.println("New state is:");
				System.out.println(currentState.toString());
				
				System.out.println("\nWaiting for opponent move...");
				
				currentState = mulinoClient.read();
				System.out.println("Current state is:");
				System.out.println(currentState.toString());
				
				Action a = minimaxDecision(currentState, MAXDEPTH);
				
				mulinoClient.write(a);
			} else {
				System.out.println("Wrong checker");
				System.exit(-1);
			}
			
			
		} while(currentState.getBlackCheckers() > 3 && currentState.getWhiteCheckers() > 3);
		
	}
	
	public static Action minimaxDecision(State state, int maxDepth) throws Exception {
		elapsedTime = System.currentTimeMillis();
		ValuedAction a = max(state, maxDepth);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Selected action is: " + a);
		return a.getAction();
	}
	
	public static ValuedAction max (State state, int maxDepth) throws Exception {
		HashMap<Action, State> successors = successors(state, player);
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp;
		
		for (Action a : successors.keySet()) {
			if (isWinningState(successors.get(a), player)) {
				result = new ValuedAction(a, Integer.MAX_VALUE);
				return result;
			}
			if (maxDepth > 1)
				temp = min((State) successors.get(a), maxDepth-1);
			else {
				State newState = null;
				switch(state.getCurrentPhase()) {
				case FIRST: newState = Phase1.applyMove(state, a, player); break;
				case SECOND: newState = Phase2.applyMove(state, a, player); break;
				case FINAL: newState = PhaseFinal.applyMove(state, a, player); break;
				default: throw new Exception("Illegal Phase");
				}
				switch(state.getCurrentPhase()) {
				case FIRST: Phase1Action action1 = (Phase1Action) a; 
							temp = new ValuedAction(a, heuristic(newState, action1.getPutPosition(), player)); 
							break;
				case SECOND: Phase2Action action2 = (Phase2Action) a; 
							temp = new ValuedAction(a, heuristic(newState, action2.getTo(), player)); 
							break;
				case FINAL: PhaseFinalAction actionFinal = (PhaseFinalAction) a; 
							temp = new ValuedAction(a, heuristic(newState, actionFinal.getTo(), player)); 
							break;
				default: throw new Exception("Illegal Phase");
				}
			}
			if (temp.getValue() > result.getValue()) {
				result = temp;
			}
		}
		return result;
	}
	
	public static ValuedAction min (State state, int maxDepth) throws Exception {
		Checker minPlayer = player==Checker.BLACK ? Checker.WHITE : Checker.BLACK;
		HashMap<Action, State> successors = successors(state, minPlayer);
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp;
		for (Action a : successors.keySet()) {
			if (isWinningState(successors.get(a), minPlayer)) {
				result = new ValuedAction(a, Integer.MIN_VALUE);
				return result;
			}
			if (maxDepth > 1) {
				temp = max((State) successors.get(a), maxDepth-1);
			}
			else {
				State newState = null;
				switch(state.getCurrentPhase()) {
				case FIRST: newState = Phase1.applyMove(state, a, minPlayer); break;
				case SECOND: newState = Phase2.applyMove(state, a, minPlayer); break;
				case FINAL: newState = PhaseFinal.applyMove(state, a, minPlayer); break;
				default: throw new Exception("Illegal Phase");
				}
				switch(state.getCurrentPhase()) {
				case FIRST: Phase1Action action1 = (Phase1Action) a; 
							temp = new ValuedAction(a, heuristic(newState, action1.getPutPosition(), player)); 
							break;
				case SECOND: Phase2Action action2 = (Phase2Action) a; 
							temp = new ValuedAction(a, heuristic(newState, action2.getTo(), player)); 
							break;
				case FINAL: PhaseFinalAction actionFinal = (PhaseFinalAction) a; 
							temp = new ValuedAction(a, heuristic(newState, actionFinal.getTo(), player)); 
							break;
				default: throw new Exception("Illegal Phase");
				}
			}
			if (temp.getValue() < result.getValue()) {
				result = temp;
			}
		}
		return result;
	}
	
	
	public static HashMap<Action, State> successors(State state, Checker p) throws Exception {
		switch(state.getCurrentPhase()) {
		case FIRST: return successorsFirst(state, p); 
		case SECOND: return successorsSecond(state, p);
		case FINAL: return successorsFinalOrSecond(state, p);
		default: throw new Exception("Illegal Phase");
		}
	}
	
	public static HashMap<Action, State> successorsFirst(State state, Checker p) {
		HashMap<Action, State> result = new HashMap<Action, State>();
		Phase1Action temp;
		State newState;
		HashMap<String, Checker> board = state.getBoard();
		
		for (String position : state.positions) {
			if (board.get(position) == State.Checker.EMPTY) {
				temp = new Phase1Action();
				temp.setPutPosition(position);
				newState = state.clone();
				newState.getBoard().put(position, p);
				try {
					if (Util.hasCompletedTriple(newState, position, p)) {
						for (String otherPosition : state.positions) {
							State.Checker otherChecker = p==Checker.WHITE ? Checker.BLACK : Checker.WHITE;
							if (board.get(otherPosition) == otherChecker) {
								temp.setRemoveOpponentChecker(otherPosition);
								newState = Phase1.applyMove(state, temp, p);
								result.put(temp, newState);
								expandedStates++;
							}
						}
					} else {
						newState = Phase1.applyMove(state, temp, p);
						result.put(temp, newState);
						expandedStates++;
					}
				} catch (WrongPhaseException | PositionNotEmptyException | NullCheckerException
						| NoMoreCheckersAvailableException | WrongPositionException | TryingToRemoveOwnCheckerException
						| TryingToRemoveEmptyCheckerException | NullStateException
						| TryingToRemoveCheckerInTripleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	public static HashMap<Action, State> successorsSecond(State state, Checker p) {
		HashMap<Action, State> result = new HashMap<Action, State>();
		Phase2Action temp;
		State newState;
		HashMap<String, Checker> board = state.getBoard();
		
		for (String position : state.positions) {
			if (board.get(position) == p) {
				temp = new Phase2Action();
				temp.setFrom(position);
				try {
					for (String adjPos : Util.getAdiacentTiles(position)) {
						if (board.get(adjPos) == Checker.EMPTY) {
							
							temp.setTo(adjPos);
							newState = state.clone();
							newState.getBoard().put(adjPos, p);
							newState.getBoard().put(position, Checker.EMPTY);
							try {
								if (Util.hasCompletedTriple(newState, adjPos, p)) {
									for (String otherPosition : state.positions) {
										State.Checker otherChecker = p==Checker.WHITE ? Checker.BLACK : Checker.WHITE;
										if (board.get(otherPosition) == otherChecker) {
											temp.setRemoveOpponentChecker(otherPosition);
											newState = Phase2.applyMove(state, temp, p);
											result.put(temp, newState);
											expandedStates++;
										}
									}
								} else {
									newState = Phase2.applyMove(state, temp, p);
									result.put(temp, newState);
									expandedStates++;
								}
							} catch (WrongPhaseException | PositionNotEmptyException | NullCheckerException
									| WrongPositionException | TryingToRemoveOwnCheckerException
									| TryingToRemoveEmptyCheckerException | NullStateException
									| TryingToRemoveCheckerInTripleException | NullActionException | TryingToMoveOpponentCheckerException | FromAndToAreEqualsException | FromAndToAreNotConnectedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
				} catch (WrongPositionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	public static HashMap<Action, State> successorsFinalOrSecond(State state, Checker p) {
		if (p == Checker.WHITE) {
			if (state.getWhiteCheckersOnBoard() > 3)
				return successorsSecond(state, p);
			else {
				return successorsFinal(state, p);
			}
		}
		//Player is BLACK
		else {
			if (state.getBlackCheckersOnBoard() > 3)
				return successorsSecond(state, p);
			else {
				return successorsFinal(state, p);
			}
		}
	}
	
	public static HashMap<Action, State> successorsFinal(State state, Checker p) {
		HashMap<Action, State> result = new HashMap<Action, State>();
		PhaseFinalAction temp;
		State newState;
		HashMap<String, Checker> board = state.getBoard();
		
		for (String position : state.positions) {
			if (board.get(position) == p) {
				temp = new PhaseFinalAction();
				temp.setFrom(position);
				for (String toPos : state.positions) {
					if (board.get(toPos) == Checker.EMPTY) {
						
						temp.setTo(toPos);
						newState = state.clone();
						newState.getBoard().put(toPos, p);
						newState.getBoard().put(position, Checker.EMPTY);
						try {
							if (Util.hasCompletedTriple(newState, toPos, p)) {
								for (String otherPosition : state.positions) {
									State.Checker otherChecker = p==Checker.WHITE ? Checker.BLACK : Checker.WHITE;
									if (board.get(otherPosition) == otherChecker) {
										temp.setRemoveOpponentChecker(otherPosition);
										newState = PhaseFinal.applyMove(state, temp, p);
										result.put(temp, newState);
										expandedStates++;
									}
								}
							} else {
								newState = PhaseFinal.applyMove(state, temp, p);
								result.put(temp, newState);
								expandedStates++;
							}
						} catch (WrongPhaseException | PositionNotEmptyException | NullCheckerException
								| WrongPositionException | TryingToRemoveOwnCheckerException
								| TryingToRemoveEmptyCheckerException | NullStateException
								| TryingToRemoveCheckerInTripleException | NullActionException | TryingToMoveOpponentCheckerException | FromAndToAreEqualsException | FromAndToAreNotConnectedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			}
		}
		
		return result;
	}
	
	public static int heuristic (State state, String position, Checker p) throws Exception {
		switch(state.getCurrentPhase()) {
		case FIRST: return heuristicPhase1(state, position, p);
		case SECOND: return heuristicPhase2(state, position, p);
		case FINAL: return heuristicPhaseFinal(state, position, p);
		default: throw new Exception("Illegal Phase");
		}
	}

	private static int heuristicPhaseFinal(State state, String position, Checker p) {
		int result = 0;
		
		if (Util.hasCompletedTriple(state, position, p)) {
			result += 16;
		}
		
		return result;
	}

	private static int heuristicPhase2(State state, String position, Checker p) {
		int result = 0;
		
		if (Util.hasCompletedTriple(state, position, p)) {
			result += 14;
		}
		
		int morrisClosed3 = 0;
		for (String pos : state.positions) {
			if (state.getBoard().get(pos) == p) {
				if (Util.hasCompletedTriple(state, pos, p)) {
					morrisClosed3++;
				}
			}
		}
		result += 43*(morrisClosed3/3);
		
		if (p == Checker.WHITE)
			result += state.getWhiteCheckersOnBoard()*8;
		else 
			result += state.getBlackCheckersOnBoard()*8;
		
		Checker otherPlayer = player==Checker.WHITE ? Checker.BLACK : Checker.WHITE;
		int blockedPieces = 0;
		for (String pos : state.positions) {
			if (state.getBoard().get(pos) == otherPlayer) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPieces++;
				}
			}
		}
		result += blockedPieces*10;
		
		return result;
	}

	private static int heuristicPhase1(State state, String position, Checker p) {
		int result=0;
		
		if (p == Checker.WHITE)
			result += state.getWhiteCheckersOnBoard()*6;
		else 
			result += state.getBlackCheckersOnBoard()*6;
		
		if (Util.hasCompletedTriple(state, position, p)) {
			result += 18;
		}
		
		int morrisClosed3 = 0;
		for (String pos : state.positions) {
			if (state.getBoard().get(pos) == p) {
				if (Util.hasCompletedTriple(state, pos, p)) {
					morrisClosed3++;
				}
			}
		}
		result += 26*(morrisClosed3/3);
		
		Checker otherPlayer = player==Checker.WHITE ? Checker.BLACK : Checker.WHITE;
		int blockedPieces = 0;
		for (String pos : state.positions) {
			if (state.getBoard().get(pos) == otherPlayer) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPieces++;
				}
			}
		}
		result += blockedPieces;
		
		return result;
	}
	
	private static boolean isWinningState(State state, Checker p) {
		if (state.getCurrentPhase() == State.Phase.FIRST)
			return false;
		if (p == Checker.WHITE && state.getBlackCheckersOnBoard() < 3)
			return true;
		else if (p == Checker.BLACK && state.getWhiteCheckersOnBoard() < 3)
			return true;
		
		Checker otherPlayer = player==Checker.WHITE ? Checker.BLACK : Checker.WHITE;
		for (String position : state.positions) {
			if (state.getBoard().get(position) == otherPlayer) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(position)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
							break;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (!isBlocked) {
					return false;
				}
			}
		}
		return true;
	}

}
