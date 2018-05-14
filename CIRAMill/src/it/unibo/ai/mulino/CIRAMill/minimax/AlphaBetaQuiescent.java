package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

public class AlphaBetaQuiescent implements IMinimax {
	private int expandedStates = 0;
	private long elapsedTime;
	private int maxHorizon = 1;
	
	private ITieChecker tieChecker;
	
	public AlphaBetaQuiescent(ITieChecker tieChecker) {
		this.tieChecker = tieChecker;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		elapsedTime = System.currentTimeMillis();
		ValuedAction valuedAction = max(state, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("AlphaBeta:");
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Selected action is: " + valuedAction);
		expandedStates = 0;
		return valuedAction;
	}
	
	private ValuedAction max(IState state, int maxDepth, int alpha, int beta) {
		List<IAction> actions = state.getFollowingMoves();
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		
		for (IAction a : actions) {
			expandedStates++;
			state.move(a);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MAX_VALUE-1);
				state.unmove(a);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = min(state, maxDepth - 1, alpha, beta);
//				state.unmove(a);
			} else {				
				// if 'quiescent' OR 'maxHorizon'
				if(state.isQuiescent() || maxHorizon == 0) {
					temp.set(a, -state.getHeuristicEvaluation());
				} else {
					maxHorizon--;
					temp = min(state, maxDepth, alpha, beta);
					maxHorizon++;
				}
			}
			
			if (temp.getValue() > result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() >= beta) {
				state.unmove(a);
				return result;
			}
			if(result.getValue() >= alpha) {
				alpha = result.getValue();
			}
			
			state.unmove(a);
		}
		
		return result;
	}
	
	private ValuedAction min(IState state, int maxDepth, int alpha, int beta) {
		List<IAction> actions = state.getFollowingMoves();
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();
		
		for (IAction a : actions) {
			expandedStates++;
			state.move(a);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MIN_VALUE+1);
				state.unmove(a);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = max(state, maxDepth - 1, alpha, beta);
//				state.unmove(a);
			} else {
				// if 'quiescent' OR 'maxHorizon'
				if(state.isQuiescent() || maxHorizon == 0) {
					temp.set(a, -state.getHeuristicEvaluation());
				} else {
					maxHorizon--;
					temp = max(state, maxDepth, alpha, beta);
					maxHorizon++;
				}
			}
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() <= alpha) {
				state.unmove(a);
				return result;
			}
			if(result.getValue() <= beta) {
				beta = result.getValue();
			}
			
			state.unmove(a);
		}
		
		return result;
	}
	
}
