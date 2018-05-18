package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

public class Negascout2 implements IMinimax{
	
	private int expandedStates = 0;
	private long elapsedTime;
	private ITieChecker tieChecker;
	
	private int originalMaxDepth;
	
	public Negascout2(ITieChecker tieChecker) {
		this.tieChecker = tieChecker;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		originalMaxDepth = maxDepth;
		
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
		System.out.println("Negascout2:");
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Selected action is: " + valuedAction);
		expandedStates = 0;
		return valuedAction;
	}

	private int negascout(IState state, int alfa, int beta, int maxDepth) {
				
		if(maxDepth <= 1)
			if ((originalMaxDepth-maxDepth)%2==0)
				return state.getHeuristicEvaluation();
			else
				return -state.getHeuristicEvaluation();
		
		List<IAction> actions = state.getFollowingMoves();
		int lo_value = alfa;
		int hi_value = beta;
		int temp;
				
		for(int i=0; i<actions.size(); i++) {
			IAction a = actions.get(i);
			expandedStates++;
			state.move(a);
			
			if(state.isWinningState()) {
				if ((originalMaxDepth-maxDepth)%2==0)
					temp = Integer.MAX_VALUE-2;
				else
					temp = Integer.MIN_VALUE+2;
				state.unmove(a);
				return temp;
			}
			else if (tieChecker.isTie(state))
				temp = 0;
			else
				temp = -negascout(state, -hi_value, -lo_value, maxDepth-1);
			
			if(temp > lo_value && temp < beta && i>0)	//re-search
				temp = -negascout(state, -beta, -temp, maxDepth-1);

			lo_value = lo_value > temp ? lo_value : temp;

			if(lo_value >= beta) {	//cut-off
				state.unmove(a);
				return lo_value;
			}
			
			hi_value = lo_value+1;	//minimal window
			state.unmove(a);
		}
		
		return lo_value;		
	}
}
