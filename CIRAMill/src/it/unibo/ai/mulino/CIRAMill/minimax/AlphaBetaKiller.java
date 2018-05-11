package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

public class AlphaBetaKiller implements IMinimax {
	private int expandedStates = 0;
	private long elapsedTime;
	private ITieChecker tieChecker;
	private int killerMovesPerLevel;
	private ValuedAction[][] killerMoves;

	public AlphaBetaKiller(ITieChecker tieChecker, int killerMovesForLevel) {
		this.tieChecker = tieChecker;
		this.killerMovesPerLevel = killerMovesForLevel;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		killerMoves = new ValuedAction[maxDepth][killerMovesPerLevel];
		
		for(int i=0; i<maxDepth; i++)
			for(int j=0; j<killerMovesPerLevel; j++)
				killerMoves[i][j] = new ValuedAction(null, i%2==0?Integer.MIN_VALUE:Integer.MAX_VALUE);
		
		elapsedTime = System.currentTimeMillis();
		ValuedAction valuedAction = max(state, maxDepth, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Selected action is: " + valuedAction);
		expandedStates = 0;
		return valuedAction;
	}
	
	private ValuedAction max(IState state, int maxDepth, int currentDepth, int alpha, int beta) {
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		
		ValuedAction killer;
		
		for(int i=0; i<killerMovesPerLevel; i++) {
			killer = killerMoves[currentDepth][i];
			
			if(killer.getAction() != null && state.isLegalMove(killer.getAction())) {
				state.move(killer.getAction());				
				if (state.isWinningState()) {
					result.set(killer.getAction(), Integer.MAX_VALUE-1);
					state.unmove(killer.getAction());
					addKillerMove(new ValuedAction(killer.getAction(), result.getValue()), currentDepth);
					return result;
				} else if (tieChecker.isTie(state)) {
					temp.set(killer.getAction(), 0);
				} else if (maxDepth > 1) {
					temp = min(state, maxDepth - 1, currentDepth + 1, alpha, beta);
				} else {
					temp.set(killer.getAction(), -state.getHeuristicEvaluation());
				}
				
				if (temp.getValue() > result.getValue()) {
					result.set(killer.getAction(), temp.getValue());
				}
				if (temp.getValue() > killer.getValue()) {
					addKillerMove(new ValuedAction(killer.getAction(), temp.getValue()), currentDepth);
				}
				if (result.getValue() >= beta) {
					state.unmove(killer.getAction());
					return result;
				}
				if(result.getValue() >= alpha) {
					alpha = result.getValue();
				}
				
				state.unmove(killer.getAction());
			}
		}
		
		List<IAction> actions = state.getFollowingMoves();
		
		for(int i=0; i<killerMovesPerLevel; i++) {
			if(killerMoves[currentDepth][i].getAction() != null)
				actions.remove(killerMoves[currentDepth][i].getAction());
		}
		
		for (IAction a : actions) {
			state.move(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MAX_VALUE-1);
				state.unmove(a);
				addKillerMove(new ValuedAction(a, result.getValue()), currentDepth);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1) {
				temp = min(state, maxDepth - 1, currentDepth + 1, alpha, beta);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() > result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (temp.getValue() > killerMoves[currentDepth][killerMovesPerLevel-1].getValue()) {
				addKillerMove(new ValuedAction(a, temp.getValue()), currentDepth);
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
	
	private ValuedAction min(IState state, int maxDepth, int currentDepth, int alpha, int beta) {
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();
		
		ValuedAction killer;
		
		for(int i=0; i<killerMovesPerLevel; i++) {
			killer = killerMoves[currentDepth][i];
			
			if(killer.getAction() != null && state.isLegalMove(killer.getAction())) {
				state.move(killer.getAction());				
				if (state.isWinningState()) {
					result.set(killer.getAction(), Integer.MIN_VALUE+1);
					state.unmove(killer.getAction());
					addKillerMove(new ValuedAction(killer.getAction(), result.getValue()), currentDepth);
					return result;
				} else if (tieChecker.isTie(state)) {
					temp.set(killer.getAction(), 0);
				} else if (maxDepth > 1) {
					temp = min(state, maxDepth - 1, currentDepth + 1, alpha, beta);
				} else {
					temp.set(killer.getAction(), -state.getHeuristicEvaluation());
				}
				
				if (temp.getValue() < result.getValue()) {
					result.set(killer.getAction(), temp.getValue());
				}
				if (temp.getValue() < killer.getValue()) {
					addKillerMove(new ValuedAction(killer.getAction(), temp.getValue()), currentDepth);
				}
				if (result.getValue() <= alpha) {
					state.unmove(killer.getAction());
					return result;
				}
				if(result.getValue() <= beta) {
					beta = result.getValue();
				}
				
				state.unmove(killer.getAction());
			}
		}
		
		List<IAction> actions = state.getFollowingMoves();
		
		for(int i=0; i<killerMovesPerLevel; i++) {
			if(killerMoves[currentDepth][i].getAction() != null)
				actions.remove(killerMoves[currentDepth][i].getAction());
		}
		
		for (IAction a : actions) {
			state.move(a);
			if (state.isWinningState()) {
				result.set(a, Integer.MIN_VALUE+1);
				state.unmove(a);
				addKillerMove(new ValuedAction(a, result.getValue()), currentDepth);
				break;
			} else if (tieChecker.isTie(state)) {
				temp.set(a, 0);
			} else if (maxDepth > 1) {
				temp = max(state, maxDepth - 1, currentDepth + 1, alpha, beta);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (temp.getValue() < killerMoves[currentDepth][killerMovesPerLevel-1].getValue()) {
				addKillerMove(new ValuedAction(a, temp.getValue()), currentDepth); 
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
	
	private void addKillerMove(ValuedAction action, int currentDepth) {
		int minMaxFactor = currentDepth%2==0?1:-1;
		
		int i;
		for(i=0; i<killerMovesPerLevel; i++) {
			if(action.getAction().equals(killerMoves[currentDepth][i].getAction()) && minMaxFactor * action.getValue() > minMaxFactor * killerMoves[currentDepth][i].getValue()) {
				
				int j;
				for(j=i-1; j>=0; j--) {
					if(minMaxFactor * action.getValue() > minMaxFactor * killerMoves[currentDepth][j].getValue()) {
						killerMoves[currentDepth][j + 1] = killerMoves[currentDepth][j];
					} else {
						break;
					}
				}
				killerMoves[currentDepth][j + 1] = action;
				
				break;
			}				
		}
		
		//This means that the move we're trying to add was not found, so we have to add it completely
		if (i == killerMovesPerLevel) {
			int index;
			
			for(index=0; index<killerMovesPerLevel; index++) {
				if(killerMoves[currentDepth][index].getAction() == null || minMaxFactor * action.getValue() > minMaxFactor * killerMoves[currentDepth][index].getValue()) {
					break;
				}
			}
			
			if(index < killerMovesPerLevel) {
				for(int i1=killerMovesPerLevel-2; i1>=index; i1--) {
					if(killerMoves[currentDepth][i1].getAction() != null)
						killerMoves[currentDepth][i1 + 1] = killerMoves[currentDepth][i1];
				}
				
				killerMoves[currentDepth][index] = action;
			}
		}
		
	}
}
