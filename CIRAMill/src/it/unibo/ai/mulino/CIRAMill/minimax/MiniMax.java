package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

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
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		
		for (IAction a : actions) {
			state.move(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MAX_VALUE-1);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1) {
				state.move(a);
				temp = min(state, maxDepth - 1);
				state.unmove(a);
			} else {
				temp.set(a, state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() > result.getValue()) {
				result.set(a, temp.getValue());
			}
		}
		
		return result;
	}
	
	private ValuedAction min(IState state, int maxDepth) {
		List<IAction> actions = state.getFollowingMoves();
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();
		
		for (IAction a : actions) {
			state.move(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MIN_VALUE-1);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1) {
				state.move(a);
				temp = max(state, maxDepth - 1);
				state.unmove(a);
			} else {
				temp.set(a, state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
		}
		
		return result;
	}

}
