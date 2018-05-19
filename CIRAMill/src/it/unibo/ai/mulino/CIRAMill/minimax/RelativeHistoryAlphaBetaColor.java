package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.Comparator;
import java.util.List;

import it.unibo.ai.mulino.CIRAMill.minimax.HistoryAlphaBeta.IActionComparator;

public class RelativeHistoryAlphaBetaColor implements IMinimax {


	private int expandedStates = 0;
	private long elapsedTime;
	private int originalMaxDepth;
	private byte player;
	
	private ITieChecker tieChecker;
	private IHistoryTableColor historyTable;
	private IHistoryTableColor butterflyTable;
	
	public RelativeHistoryAlphaBetaColor(ITieChecker tieChecker, IHistoryTableColor historyTable, IHistoryTableColor butterflyTable) {
		this.tieChecker = tieChecker;
		this.historyTable = historyTable;
		this.butterflyTable = butterflyTable;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		originalMaxDepth = maxDepth;
		elapsedTime = System.currentTimeMillis();
		ValuedAction valuedAction = max(state, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("RelativeHistoryAlphaBetaColor:");
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Selected action is: " + valuedAction);
//		System.out.println(tieChecker);
		expandedStates = 0;
		return valuedAction;
	}
	
	private ValuedAction max(IState state, int maxDepth, int alpha, int beta) {
		List<IAction> actions = state.getFollowingMoves();
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		
		player = 0;
		actions.sort(new ActionComparator());
		
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
				player = 0;
//				state.unmove(a);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() > result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() >= beta) {
				state.unmove(a);
				historyTable.incrementValue(result.getAction(), maxDepth, player);
				return result;
			} else {
				butterflyTable.incrementValue(result.getAction(), maxDepth, player);
			}
			if(result.getValue() >= alpha) {
				alpha = result.getValue();
			}
			
			state.unmove(a);
		}
		
		if(result.getAction() != null)
			historyTable.incrementValue(result.getAction(), maxDepth, player);
		return result;
	}
	
	private ValuedAction min(IState state, int maxDepth, int alpha, int beta) {
		List<IAction> actions = state.getFollowingMoves();
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();
		
		player = 1;
		actions.sort(new ActionComparator());
		
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
				player = 1;
//				state.unmove(a);
			} else {
				temp.set(a, state.getHeuristicEvaluation());
			}
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() <= alpha) {
				state.unmove(a);
				historyTable.incrementValue(result.getAction(), maxDepth, player);
				return result;
			} else {
				butterflyTable.incrementValue(result.getAction(), maxDepth, player);
			}
			if(result.getValue() <= beta) {
				beta = result.getValue();
			}
			
			state.unmove(a);
		}
		
		if(result.getAction() != null)
			historyTable.incrementValue(result.getAction(), maxDepth, player);
		return result;
	}
	
	class ActionComparator implements Comparator<IAction> {
		
		@Override
		public int compare(IAction arg0, IAction arg1) {			
			return historyTable.getValue(arg0, player) / butterflyTable.getValue(arg0, player) == historyTable.getValue(arg1, player) / butterflyTable.getValue(arg1, player) ? 0 : historyTable.getValue(arg0, player) / butterflyTable.getValue(arg0, player) > historyTable.getValue(arg1, player) / butterflyTable.getValue(arg1, player) ? -1 : 1;
		}
		
	}

}
