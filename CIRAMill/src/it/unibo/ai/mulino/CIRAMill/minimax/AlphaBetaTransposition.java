package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

public class AlphaBetaTransposition implements IMinimax {
	
	private int expandedStates = 0;
	private long elapsedTime;
	
	private ITieChecker tieChecker;
	private ITranspositionTable tranpositionTable;
	
	public AlphaBetaTransposition(ITieChecker tieChecker, ITranspositionTable transpositionTable) {
		this.tieChecker = tieChecker;
		this.tranpositionTable = transpositionTable;
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
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		
		//Tranposition Handling
		ValuedAction[] transpActions = tranpositionTable.getValuedActions(state);
		IAction action;
		for (int i=0; i<transpActions.length; i++) {
			expandedStates++;
			action = transpActions[i].getAction();
			state.move(action);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(action, Integer.MAX_VALUE-1);
				state.unmove(action);
				tranpositionTable.putValuedAction(state, result, maxDepth);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(action, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = min(state, maxDepth - 1, alpha, beta);
//				state.unmove(a);
			} else {
				temp.set(action, -state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() > result.getValue()) {
				result.set(action, temp.getValue());
			}
			if (result.getValue() >= beta) {
				state.unmove(action);
				tranpositionTable.putValuedAction(state, result, maxDepth);
				return result;
			}
			if(result.getValue() >= alpha) {
				alpha = result.getValue();
			}
			
			state.unmove(action);
		}

		List<IAction> actions = state.getFollowingMoves();
		for (int i=0; i<transpActions.length; i++) {
			actions.remove(transpActions[i].getAction());
		}
		for (IAction a : actions) {
			expandedStates++;
			state.move(a);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MAX_VALUE-1);
				state.unmove(a);
				tranpositionTable.putValuedAction(state, result, maxDepth);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = min(state, maxDepth - 1, alpha, beta);
//				state.unmove(a);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() > result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() >= beta) {
				state.unmove(a);
				tranpositionTable.putValuedAction(state, result, maxDepth);
				return result;
			}
			if(result.getValue() >= alpha) {
				alpha = result.getValue();
			}
			
			state.unmove(a);
		}

		tranpositionTable.putValuedAction(state, result, maxDepth);
		return result;
	}
	
	private ValuedAction min(IState state, int maxDepth, int alpha, int beta) {
		List<IAction> actions = state.getFollowingMoves();
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();
		
		//Tranposition Handling
		ValuedAction[] transpActions = tranpositionTable.getValuedActions(state);
		IAction action;
		for (int i=0; i<transpActions.length; i++) {
			expandedStates++;
			action = transpActions[i].getAction();
			state.move(action);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(action, Integer.MIN_VALUE+1);
				state.unmove(action);
				tranpositionTable.putValuedAction(state, result, maxDepth);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(action, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = max(state, maxDepth - 1, alpha, beta);
//				state.unmove(a);
			} else {
				temp.set(action, -state.getHeuristicEvaluation());
			}
			if (temp.getValue() < result.getValue()) {
				result.set(action, temp.getValue());
			}
			if (result.getValue() <= alpha) {
				state.unmove(action);
				tranpositionTable.putValuedAction(state, result, maxDepth);
				return result;
			}
			if(result.getValue() <= beta) {
				beta = result.getValue();
			}
			
			state.unmove(action);
		}
		
		for (IAction a : actions) {
			expandedStates++;
			state.move(a);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MIN_VALUE+1);
				state.unmove(a);
				tranpositionTable.putValuedAction(state, result, maxDepth);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = max(state, maxDepth - 1, alpha, beta);
//				state.unmove(a);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() <= alpha) {
				state.unmove(a);
				tranpositionTable.putValuedAction(state, result, maxDepth);
				return result;
			}
			if(result.getValue() <= beta) {
				beta = result.getValue();
			}
			
			state.unmove(a);
		}

		tranpositionTable.putValuedAction(state, result, maxDepth);
		return result;
	}

}
