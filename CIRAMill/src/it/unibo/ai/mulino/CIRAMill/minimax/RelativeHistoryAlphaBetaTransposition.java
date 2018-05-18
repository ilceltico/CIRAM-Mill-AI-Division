package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.Comparator;
import java.util.List;

import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;
import it.unibo.ai.mulino.CIRAMill.minimax.RelativeHistoryAlphaBetaColor.ActionComparator;

public class RelativeHistoryAlphaBetaTransposition implements IMinimax {
	
	private int expandedStates = 0;
	private int ttHits = 0;
	private long elapsedTime;
	
	private ITieChecker tieChecker;
	private ITranspositionTable tranpositionTable;
	private IHistoryTable historyTable;
	private IHistoryTable butterflyTable;
	
	public RelativeHistoryAlphaBetaTransposition(ITieChecker tieChecker, ITranspositionTable transpositionTable, IHistoryTable historyTable, IHistoryTable butterflyTable) {
		this.tieChecker = tieChecker;
		this.tranpositionTable = transpositionTable;
		this.historyTable = historyTable;
		this.butterflyTable = butterflyTable;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		elapsedTime = System.currentTimeMillis();
		ValuedAction valuedAction = max(state, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("RelativeHistoryAlphaBetaTransposition:");
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
				historyTable.incrementValue(result.getAction(), maxDepth);
				return result;
			} else {
				butterflyTable.incrementValue(result.getAction(), maxDepth);
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
		
		actions.sort(new ActionComparator());
		
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
				historyTable.incrementValue(result.getAction(), maxDepth);
				return result;
			} else {
				butterflyTable.incrementValue(result.getAction(), maxDepth);
			}
			if(result.getValue() >= alpha) {
				alpha = result.getValue();
			}
			
			state.unmove(a);
		}
		
		if (result.getAction() != null) {
			tranpositionTable.putAction(state, result.getAction(), maxDepth);
			historyTable.incrementValue(result.getAction(), maxDepth);
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
				temp.set(action, -state.getHeuristicEvaluation());
			}
			if (temp.getValue() < result.getValue()) {
				result.set(action, temp.getValue());
			}
			if (result.getValue() <= alpha) {
				state.unmove(action);
				tranpositionTable.putAction(state, action, maxDepth);
				historyTable.incrementValue(result.getAction(), maxDepth);
				return result;
			} else {
				butterflyTable.incrementValue(result.getAction(), maxDepth);
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
		
		actions.sort(new ActionComparator());
		
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
				temp.set(a, -state.getHeuristicEvaluation());
			}
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() <= alpha) {
				state.unmove(a);
				tranpositionTable.putAction(state, a, maxDepth);
				historyTable.incrementValue(result.getAction(), maxDepth);
				return result;
			} else {
				butterflyTable.incrementValue(result.getAction(), maxDepth);
			}
			if(result.getValue() <= beta) {
				beta = result.getValue();
			}
			
			state.unmove(a);
		}
		
		if (result.getAction() != null) {
			tranpositionTable.putAction(state, result.getAction(), maxDepth);
			historyTable.incrementValue(result.getAction(), maxDepth);
		}
		return result;
	}
	
	class ActionComparator implements Comparator<IAction> {
		
		@Override
		public int compare(IAction arg0, IAction arg1) {			
			return historyTable.getValue(arg0) / butterflyTable.getValue(arg0) == historyTable.getValue(arg1) / butterflyTable.getValue(arg1) ? 0 : historyTable.getValue(arg0) / butterflyTable.getValue(arg0) > historyTable.getValue(arg1) / butterflyTable.getValue(arg1) ? -1 : 1;
		}
		
	}

}
