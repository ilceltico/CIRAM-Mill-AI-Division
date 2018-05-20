package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.Comparator;
import java.util.List;

public class NegascoutTranspositionRelativeHistory implements IMinimax{
	
	private int expandedStates = 0;
	private long elapsedTime;
	private int ttHits = 0;
	
	private ITieChecker tieChecker;
	private IHistoryTable historyTable;
	private IHistoryTable butterflyTable;
	private ITranspositionTable transpositionTable;

	public NegascoutTranspositionRelativeHistory(ITieChecker tieChecker, ITranspositionTable transpositionTable, IHistoryTable historyTable, IHistoryTable butterflyTable) {
		this.tieChecker = tieChecker;
		this.historyTable = historyTable;
		this.transpositionTable = transpositionTable;
		this.butterflyTable = butterflyTable;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		elapsedTime = System.currentTimeMillis();
		
		ValuedAction valuedAction = evaluate(state, maxDepth);
	
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("Negascout Transposition Relative History:");
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("TT Hits: " + ttHits);
		System.out.println("Selected action is: " + valuedAction);
		expandedStates = 0;
		ttHits = 0;
		return valuedAction;
	}
	
	private ValuedAction evaluate(IState state, int maxDepth) {
		
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		
//		Transposition Handling
		IAction[] transpActions = transpositionTable.getActions(state);
		IAction action;
		if(transpActions.length > 0)
			ttHits++;
		for(int i=0; i<transpActions.length; i++) {
			expandedStates++;
			action = transpActions[i];
			state.move(action);
			if(state.isWinningState()) {
				result.set(action, Integer.MAX_VALUE-2);
				state.unmove(action);
				transpositionTable.putAction(state, action, maxDepth);
				historyTable.incrementValue(action, maxDepth);
				return result;
			} else if(tieChecker.isTie(state)) {
				temp.set(action, 0);
			} else if (maxDepth > 1){
				temp.set(action, -negascout(state, Integer.MIN_VALUE+1, Integer.MAX_VALUE-1, maxDepth));
			} else {
				temp.set(action, -state.getHeuristicEvaluation());
			}
			
			if(temp.getValue() > result.getValue())
				result.set(temp.getAction(), temp.getValue());
			
			butterflyTable.incrementValue(action, maxDepth);
			state.unmove(action);
		}
		
		List<IAction> actions = state.getFollowingMoves();
		for (int i=0; i<transpActions.length; i++) {
			actions.remove(transpActions[i]);
		}
		
		actions.sort(new ActionComparator());
		
		for(IAction a : actions) {
			expandedStates++;
			state.move(a);
			if(state.isWinningState()) {
				result.set(a, Integer.MAX_VALUE-2);
				state.unmove(a);
				break;
			} else if(tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1){
				temp.set(a, -negascout(state, Integer.MIN_VALUE+1, Integer.MAX_VALUE-1, maxDepth));
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			
			if(temp.getValue() > result.getValue())
				result.set(temp.getAction(), temp.getValue());
			
			butterflyTable.incrementValue(a, maxDepth);
			state.unmove(a);
		}
		
		if (result.getAction() != null) {			
			transpositionTable.putAction(state, result.getAction(), maxDepth);
			historyTable.incrementValue(result.getAction(), maxDepth);
		}
			
		return result;
	}

	private int negascout(IState state, int alfa, int beta, int maxDepth) {
				
		if(maxDepth <= 1)
			return state.getHeuristicEvaluation();
//			if ((originalMaxDepth-maxDepth)%2==0)
//				return state.getHeuristicEvaluation();
//			else
//				return -state.getHeuristicEvaluation();
		
		int lo_value = alfa;
		int hi_value = beta;
		int temp;
		
//		Transposition Handling
		IAction[] transpActions = transpositionTable.getActions(state);
		IAction action;
		
		if(transpActions.length > 0)
			ttHits++;
		for(int i=0; i<transpActions.length; i++) {
			expandedStates++;
			action = transpActions[i];
			state.move(action);
			
			if(state.isWinningState()) {
				temp = Integer.MAX_VALUE-2;
//				if ((originalMaxDepth-maxDepth)%2==0)
//					temp = Integer.MAX_VALUE-2;
//				else
//					temp = Integer.MIN_VALUE+2;
				
				state.unmove(action);
				transpositionTable.putAction(state, action, maxDepth);
				historyTable.incrementValue(action, maxDepth);
				return temp;
			} else if(tieChecker.isTie(state))
				temp = 0;
			else
				temp = -negascout(state, -hi_value, -lo_value, maxDepth-1);
		
			if(temp > lo_value && temp < beta && i>0)	//re-search
				temp = -negascout(state, -beta, -temp, maxDepth-1);

			lo_value = lo_value > temp ? lo_value : temp;

			if(lo_value >= beta) {	//cut-off
				state.unmove(action);
				transpositionTable.putAction(state, action, maxDepth);
				historyTable.incrementValue(action, maxDepth);
				return lo_value;
			} else {
				butterflyTable.incrementValue(action, maxDepth);
			}
			
			hi_value = lo_value+1;	//minimal window
			state.unmove(action);
		}
		
		IAction bestAction = null;
		List<IAction> actions = state.getFollowingMoves();
		for (int i=0; i<transpActions.length; i++) {
			actions.remove(transpActions[i]);
		}
		
		actions.sort(new ActionComparator());
		
		for(int i=0; i<actions.size(); i++) {
			IAction a = actions.get(i);
			expandedStates++;
			state.move(a);
			
			if(state.isWinningState()) {
				temp = Integer.MAX_VALUE-2;
//				if ((originalMaxDepth-maxDepth)%2==0)
//					temp = Integer.MAX_VALUE-2;
//				else
//					temp = Integer.MIN_VALUE+2;
				
				state.unmove(a);
				transpositionTable.putAction(state, a, maxDepth);
				historyTable.incrementValue(a, maxDepth);
				return temp;
			}
			else if (tieChecker.isTie(state))
				temp = 0;
			else
				temp = -negascout(state, -hi_value, -lo_value, maxDepth-1);
			
			if(temp > lo_value && temp < beta && i>0)	//re-search
				temp = -negascout(state, -beta, -temp, maxDepth-1);

//			lo_value = lo_value > temp ? lo_value : temp;
			if(temp > lo_value) {
				lo_value = temp;
				bestAction = a;
			}

			if(lo_value >= beta) {	//cut-off
				state.unmove(a);
				transpositionTable.putAction(state, a, maxDepth);
				historyTable.incrementValue(a, maxDepth);
				return lo_value;
			} else {
				butterflyTable.incrementValue(a, maxDepth);
			}
			
			hi_value = lo_value+1;	//minimal window
			state.unmove(a);
		}
		
//		devo incrementare il valore in history dell'azione migliore.
		if (bestAction != null) {
			transpositionTable.putAction(state, bestAction, maxDepth);
			historyTable.incrementValue(bestAction, maxDepth);
		}
		
		return lo_value;		
	}
	
	class ActionComparator implements Comparator<IAction> {
		
		@Override
		public int compare(IAction arg0, IAction arg1) {			
			return historyTable.getValue(arg0) / butterflyTable.getValue(arg0) == historyTable.getValue(arg1) / butterflyTable.getValue(arg1) ? 0 : historyTable.getValue(arg0) / butterflyTable.getValue(arg0) > historyTable.getValue(arg1) / butterflyTable.getValue(arg1) ? -1 : 1;
		}
		
	}
}
