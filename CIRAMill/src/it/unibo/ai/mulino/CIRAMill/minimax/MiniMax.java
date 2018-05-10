package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;

public class MiniMax implements IMinimax {
	
	private int expandedStates = 0;
	private long elapsedTime;
	
	private ITieChecker tieChecker;
	
	public MiniMax(ITieChecker tieChecker) {
		this.tieChecker = tieChecker;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		elapsedTime = System.currentTimeMillis();
		ValuedAction valuedAction = max(state, maxDepth);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Selected action is: " + valuedAction);
		expandedStates = 0;
		return valuedAction;
	}
	
	private ValuedAction max(IState state, int maxDepth) {
		List<IAction> actions = state.getFollowingMoves();
		expandedStates += actions.size();
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		
		for (IAction a : actions) {
			state.move(a);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MAX_VALUE-1);
				state.unmove(a);
				break;
			} else if (tieChecker.isTie(state)) {
//				System.out.println("tie");
				temp.set(a, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = min(state, maxDepth - 1);
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
			if (state.isWinningState()) {
				result.set(a, Integer.MIN_VALUE-1);
				state.unmove(a);
				break;
			} else if (tieChecker.isTie(state)) {
//				System.out.println("tie");
				temp.set(a, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = max(state, maxDepth - 1);
//				state.unmove(a);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
			state.unmove(a);
		}
		
		return result;
	}

}
