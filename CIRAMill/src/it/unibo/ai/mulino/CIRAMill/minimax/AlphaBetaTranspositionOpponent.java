package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

public class AlphaBetaTranspositionOpponent implements IMinimaxOpponent {
	
	private int expandedStates = 0;
	private int ttHits = 0;
	private long elapsedTime;
	
	private int initialMaxDepth;
	private ValuedAction opponentProbableAction;
	
	private ITieChecker tieChecker;
	private ITranspositionTable tranpositionTable;
	
	public AlphaBetaTranspositionOpponent(ITieChecker tieChecker, ITranspositionTable transpositionTable) {
		this.tieChecker = tieChecker;
		this.tranpositionTable = transpositionTable;
	}
	
	@Override
	public ValuedAction getOpponentValuedAction() {
		return opponentProbableAction;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		initialMaxDepth = maxDepth;
		expandedStates = 0;
		ttHits = 0;
		elapsedTime = System.currentTimeMillis();
		ValuedAction valuedAction = max(state, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("AlphaBetaTransposition:");
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("TT Hits: " + ttHits);
		System.out.println("Selected action is: " + valuedAction);
		expandedStates = 0;
		ttHits = 0;
		return valuedAction;
	}
	
	private ValuedAction max(IState state, int maxDepth, int alpha, int beta) {
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		
		//Tranposition Handling
		IAction[] transpActions = tranpositionTable.getActions(state);
		IAction action;
		if (transpActions.length > 0)
			ttHits++;
		for (int i=0; i<transpActions.length; i++) {
			expandedStates++;
			action = transpActions[i];
			state.move(action);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(action, Integer.MAX_VALUE-1);
				state.unmove(action);
				tranpositionTable.putAction(state, action, maxDepth);
				
				return result;
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
				tranpositionTable.putAction(state, action, maxDepth);
				if (maxDepth == initialMaxDepth) {
					this.opponentProbableAction = new ValuedAction(temp.getAction(), temp.getValue());
				}
				return result;
			}
			if(result.getValue() >= alpha) {
				alpha = result.getValue();
			}
			
			state.unmove(action);
		}

		List<IAction> actions = state.getFollowingMoves();
		for (int i=0; i<transpActions.length; i++) {
			actions.remove(transpActions[i]);
		}
		for (IAction a : actions) {
			expandedStates++;
			state.move(a);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MAX_VALUE-1);
				state.unmove(a);
				tranpositionTable.putAction(state, a, maxDepth);
				return result;
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
				tranpositionTable.putAction(state, a, maxDepth);
				if (maxDepth == initialMaxDepth) {
					this.opponentProbableAction = new ValuedAction(temp.getAction(), temp.getValue());
				}
				return result;
			}
			if(result.getValue() >= alpha) {
				alpha = result.getValue();
			}
			
			state.unmove(a);
		}
		
		if (result.getAction() != null)
			tranpositionTable.putAction(state, result.getAction(), maxDepth);
		if (maxDepth == initialMaxDepth) {
			this.opponentProbableAction = new ValuedAction(temp.getAction(), temp.getValue());
		}
		return result;
	}
	
	private ValuedAction min(IState state, int maxDepth, int alpha, int beta) {
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();

		//Tranposition Handling
		IAction[] transpActions = tranpositionTable.getActions(state);
		IAction action;
		if (transpActions.length > 0)
			ttHits++;
		for (int i=0; i<transpActions.length; i++) {
			expandedStates++;
			action = transpActions[i];
			state.move(action);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(action, Integer.MIN_VALUE+1);
				state.unmove(action);
				tranpositionTable.putAction(state, action, maxDepth);
				return result;
			} else if (tieChecker.isTie(state)) {
				temp.set(action, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = max(state, maxDepth - 1, alpha, beta);
//				state.unmove(a);
			} else {
				temp.set(action, state.getHeuristicEvaluation());
			}
			if (temp.getValue() < result.getValue()) {
				result.set(action, temp.getValue());
			}
			if (result.getValue() <= alpha) {
				state.unmove(action);
				tranpositionTable.putAction(state, action, maxDepth);
				return result;
			}
			if(result.getValue() <= beta) {
				beta = result.getValue();
			}
			
			state.unmove(action);
		}
		
		List<IAction> actions = state.getFollowingMoves();
		for (int i=0; i<transpActions.length; i++) {
			actions.remove(transpActions[i]);
		}
		for (IAction a : actions) {
			expandedStates++;
			state.move(a);
//			BitBoardState newState = (BitBoardState) state.applyMove(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MIN_VALUE+1);
				state.unmove(a);
				tranpositionTable.putAction(state, a, maxDepth);
				return result;
			} else if (tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1) {
//				state.move(a);
				temp = max(state, maxDepth - 1, alpha, beta);
//				state.unmove(a);
			} else {
				temp.set(a, state.getHeuristicEvaluation());
			}
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() <= alpha) {
				state.unmove(a);
				tranpositionTable.putAction(state, a, maxDepth);
				return result;
			}
			if(result.getValue() <= beta) {
				beta = result.getValue();
			}
			
			state.unmove(a);
		}
		
		if (result.getAction() != null)
			tranpositionTable.putAction(state, result.getAction(), maxDepth);
		return result;
	}

}
