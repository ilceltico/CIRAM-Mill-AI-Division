package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.didattica.mulino.actions.Util;
import it.unibo.ai.didattica.mulino.actions.WrongPositionException;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;

public class MiniMax implements IMinimax {
	
	private int expandedStates = 0;
	private long elapsedTime;	
	private int tieCount = 0;
	private int winCount = 0;
	private ITieChecker tieChecker;
	
	public MiniMax(ITieChecker tieChecker) {
		this.tieChecker = tieChecker;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		elapsedTime = System.currentTimeMillis();
		ValuedAction valuedAction = max(state, maxDepth);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("MiniMax:");
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Tie Count: " + tieCount);
		System.out.println("Win count: " + winCount);
		System.out.println("Selected action is: " + valuedAction);
		expandedStates = 0;
		
		tieCount = 0;
		winCount = 0;
		statesAlreadySeen = new ArrayList<>();
		
		return valuedAction;
	}
	
	public static boolean isWinningStateOld(State state, Checker p) {
		if (state.getCurrentPhase() == State.Phase.FIRST)
			return false;
		if (p == Checker.WHITE && state.getBlackCheckersOnBoard() < 3)
			return true;
		else if (p == Checker.BLACK && state.getWhiteCheckersOnBoard() < 3)
			return true;
		
		if (state.getCurrentPhase() == State.Phase.SECOND) {
			Checker otherPlayer = p == Checker.WHITE ? Checker.BLACK : Checker.WHITE;
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
		return false;
	}
	
	private ArrayList<IState> statesAlreadySeen = new ArrayList<>();
	private ValuedAction max(IState state, int maxDepth) {
		List<IAction> actions = state.getFollowingMoves();
		expandedStates += actions.size();
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		
		for (IAction a : actions) {
			state.move(a);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState() || isWinningStateOld(((BitBoardState) state).fromBitBoardToState(), ((BitBoardState)state).playerToMove==BitBoardState.WHITE?Checker.BLACK:Checker.WHITE) ) {
				if (state.isWinningState() && !isWinningStateOld(((BitBoardState) state).fromBitBoardToState(), ((BitBoardState)state).playerToMove==BitBoardState.WHITE?Checker.BLACK:Checker.WHITE))
					throw new IllegalArgumentException();
				if (isWinningStateOld(((BitBoardState) state).fromBitBoardToState(), ((BitBoardState)state).playerToMove==BitBoardState.WHITE?Checker.BLACK:Checker.WHITE) && !state.isWinningState())
					throw new IllegalArgumentException();
				result.set(a, Integer.MAX_VALUE-1);
				winCount++;
				state.unmove(a);
				return result;
			} else if (tieChecker.isTie(state) 
					|| statesAlreadySeen.contains(state)
					) {
//				System.out.println("tie");
				tieCount++;
				if (tieChecker.isTie(state) && !statesAlreadySeen.contains(state)) {
					throw new IllegalArgumentException();
				}
				if (!tieChecker.isTie(state) && statesAlreadySeen.contains(state)) {
					throw new IllegalArgumentException();
				}
				temp.set(a, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				statesAlreadySeen.add(state.clone());
				temp = min(state, maxDepth - 1);
				statesAlreadySeen.remove(state.clone());
//				state.unmove(a);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() > result.getValue()) {
				result.set(a, temp.getValue());
			}
			
			state.unmove(a);
		}
		
		return result;
	}
	
	private ValuedAction min(IState state, int maxDepth) {
		List<IAction> actions = state.getFollowingMoves();
		expandedStates += actions.size();
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();
		
		for (IAction a : actions) {
			state.move(a);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState() || isWinningStateOld(((BitBoardState) state).fromBitBoardToState(), ((BitBoardState)state).playerToMove==BitBoardState.WHITE?Checker.BLACK:Checker.WHITE) ) {
				if (state.isWinningState() && !isWinningStateOld(((BitBoardState) state).fromBitBoardToState(), ((BitBoardState)state).playerToMove==BitBoardState.WHITE?Checker.BLACK:Checker.WHITE))
					throw new IllegalArgumentException();
				if (isWinningStateOld(((BitBoardState) state).fromBitBoardToState(), ((BitBoardState)state).playerToMove==BitBoardState.WHITE?Checker.BLACK:Checker.WHITE) && !state.isWinningState())
					throw new IllegalArgumentException();
				result.set(a, Integer.MIN_VALUE+1);
				winCount++;
				state.unmove(a);
				return result;
			} else if (tieChecker.isTie(state) 
					|| statesAlreadySeen.contains(state)
					) {
//				System.out.println("tie");
				tieCount++;
				if (tieChecker.isTie(state) && !statesAlreadySeen.contains(state)) {
					throw new IllegalArgumentException();
				}
				if (!tieChecker.isTie(state) && statesAlreadySeen.contains(state)) {
					throw new IllegalArgumentException();
				}
				temp.set(a, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				statesAlreadySeen.add(state.clone());
				temp = max(state, maxDepth - 1);
				statesAlreadySeen.remove(state.clone());
//				state.unmove(a);
			} else {
				temp.set(a, state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
			state.unmove(a);
		}
		
		return result;
	}

}
