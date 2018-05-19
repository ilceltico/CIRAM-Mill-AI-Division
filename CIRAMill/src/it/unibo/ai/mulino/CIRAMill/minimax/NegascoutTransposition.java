package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

public class NegascoutTransposition implements IMinimax{
	
	private int expandedStates = 0;
	private long elapsedTime;
	private int ttHits = 0;
	
	private ITieChecker tieChecker;
	private ITranspositionTable transpositionTable;
	
	public NegascoutTransposition(ITieChecker tieChecker, ITranspositionTable transpositionTable) {
		this.tieChecker = tieChecker;
		this.transpositionTable = transpositionTable;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		elapsedTime = System.currentTimeMillis();
		
		List<IAction> actions = state.getFollowingMoves();
		ValuedAction valuedAction = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		for(IAction a : actions) {
			expandedStates++;
			state.move(a);
			if(state.isWinningState()) {
				valuedAction.set(a, Integer.MAX_VALUE-2);
				state.unmove(a);
				break;
			} else if(tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1){
				temp.set(a, -negascout(state, Integer.MIN_VALUE+1, Integer.MAX_VALUE-1, maxDepth));
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			
			if(temp.getValue() > valuedAction.getValue())
				valuedAction.set(temp.getAction(), temp.getValue());
			
			state.unmove(a);
		}

		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("Negascout Transposition:");
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("TT Hits: " + ttHits);
		System.out.println("Selected action is: " + valuedAction);
		expandedStates = 0;
		ttHits = 0;
		return valuedAction;
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
		if(transpActions.length>0)
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
				return temp;
			} 
			else if(tieChecker.isTie(state))
				temp = 0;
			else
				temp = -negascout(state, -hi_value, -lo_value, maxDepth-1);
			
			if(temp > lo_value && temp < beta && i>0)	//re-search
				temp = -negascout(state, -beta, -temp, maxDepth-1);
			
			lo_value = lo_value > temp ? lo_value : temp;
			
			if(lo_value >= beta) {	//cut-off
				state.unmove(action);
				transpositionTable.putAction(state, action, maxDepth);
				return lo_value;
			}
			
			hi_value = lo_value+1;	//minimal window
			state.unmove(action);
		}
				
		List<IAction> actions = state.getFollowingMoves();
		IAction bestAction = null;
		for (int i=0; i<transpActions.length; i++) {
			actions.remove(transpActions[i]);
		}
		for(int i=0; i<actions.size(); i++) {
			expandedStates++;
			action = actions.get(i);
			state.move(action);
			
			if(state.isWinningState()) {
				temp = Integer.MAX_VALUE-2;
//				if ((originalMaxDepth-maxDepth)%2==0)
//					temp = Integer.MAX_VALUE-2;
//				else
//					temp = Integer.MIN_VALUE+2;
				
				state.unmove(action);
				transpositionTable.putAction(state, action, maxDepth);
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
				bestAction = action;
			}

			if(lo_value >= beta) {	//cut-off
				state.unmove(action);
				transpositionTable.putAction(state, action, maxDepth);
				return lo_value;
			}
			
			hi_value = lo_value+1;	//minimal window
			state.unmove(action);
		}
		
		if(bestAction != null)
			transpositionTable.putAction(state, bestAction, maxDepth);
			
		return lo_value;		
	}
}
