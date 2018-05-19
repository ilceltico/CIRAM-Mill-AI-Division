package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

/*
 * This class implements the Killer Heuristic in a very similar way to AlphaBetaKiller, but has (up to now) 
 * proven to be quicker. 
 * It doesn't add (or update) every evaluated move to the killer array. Instead, it adds (or updates) only the best move 
 * for the evaluated state.
 */
public class AlphaBetaKillerVariant implements IMinimax {
	private int expandedStates = 0;
	private int killerArrayHits = 0;
	private long elapsedTime;
	private ITieChecker tieChecker;
	private int killerMovesPerLevel;
	private ValuedAction[][] killerMoves;

	public AlphaBetaKillerVariant(ITieChecker tieChecker, int killerMovesForLevel) {
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
		System.out.println("AlphaBetaKillerVariant:");
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Killer hits: " + killerArrayHits);
		System.out.println("Selected action is: " + valuedAction);
//		printKillerMoves();
		expandedStates = 0;
		killerArrayHits = 0;
		return valuedAction;
	}
	
	private ValuedAction max(IState state, int maxDepth, int currentDepth, int alpha, int beta) {
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();
		List<IAction> actions = state.getFollowingMoves();
		
		ValuedAction killer;
		
		for(int i=0; i<killerMovesPerLevel; i++) {
			killer = killerMoves[currentDepth][i];
			
			if(killer.getAction() != null && actions.contains(killer.getAction())) {
				expandedStates++;
				killerArrayHits++;
				actions.remove(killer.getAction());
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
		
		
		for (IAction a : actions) {
			expandedStates++;
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
//			if (temp.getValue() > killerMoves[currentDepth][killerMovesPerLevel-1].getValue()) {
//				addKillerMove(new ValuedAction(a, temp.getValue()), currentDepth);
//			}
			if (result.getValue() >= beta) {
				state.unmove(a);
				if (temp.getValue() > killerMoves[currentDepth][killerMovesPerLevel-1].getValue()) {
					addKillerMove(new ValuedAction(a, temp.getValue()), currentDepth);
				}
				return result;
			}
			if(result.getValue() >= alpha) {
				alpha = result.getValue();
			}
			
			state.unmove(a);
		}
		
		if (result.getValue() > killerMoves[currentDepth][killerMovesPerLevel-1].getValue()) {
			addKillerMove(new ValuedAction(result.getAction(), result.getValue()), currentDepth);
		}
		
		return result;
	}
	
	private ValuedAction min(IState state, int maxDepth, int currentDepth, int alpha, int beta) {
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();
		List<IAction> actions = state.getFollowingMoves();
		
		ValuedAction killer;

		for(int i=0; i<killerMovesPerLevel; i++) {
			killer = killerMoves[currentDepth][i];

			if(killer.getAction() != null && actions.contains(killer.getAction())) {
				expandedStates++;
				killerArrayHits++;
				actions.remove(killer.getAction());
				state.move(killer.getAction());				
				if (state.isWinningState()) {
					result.set(killer.getAction(), Integer.MIN_VALUE+1);
					state.unmove(killer.getAction());
					addKillerMove(new ValuedAction(killer.getAction(), result.getValue()), currentDepth);
					return result;
				} else if (tieChecker.isTie(state)) {
					temp.set(killer.getAction(), 0);
				} else if (maxDepth > 1) {
					temp = max(state, maxDepth - 1, currentDepth + 1, alpha, beta);
				} else {
					temp.set(killer.getAction(), state.getHeuristicEvaluation());
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
		
		
		for (IAction a : actions) {
			expandedStates++;
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
				temp.set(a, state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
//			if (temp.getValue() < killerMoves[currentDepth][killerMovesPerLevel-1].getValue()) {
//				addKillerMove(new ValuedAction(a, temp.getValue()), currentDepth); 
//			}
			if (result.getValue() <= alpha) {
				state.unmove(a);
				if (temp.getValue() < killerMoves[currentDepth][killerMovesPerLevel-1].getValue()) {
					addKillerMove(new ValuedAction(a, temp.getValue()), currentDepth); 
				}
				return result;
			}
			if(result.getValue() <= beta) {
				beta = result.getValue();
			}
			
			state.unmove(a);
		}
		
		if (result.getValue() < killerMoves[currentDepth][killerMovesPerLevel-1].getValue()) {
			addKillerMove(new ValuedAction(result.getAction(), result.getValue()), currentDepth); 
		}
		
		return result;
	}
	
	private void addKillerMove(ValuedAction action, int currentDepth) {
		int minMaxFactor = currentDepth%2==0?1:-1;
		
		int i;
		for(i=0; i<killerMovesPerLevel; i++) {
			if(action.getAction().equals(killerMoves[currentDepth][i].getAction())) {
				if(minMaxFactor * action.getValue() > minMaxFactor * killerMoves[currentDepth][i].getValue()) {
					int j;
					for(j=i-1; j>=0; j--) {
						if(minMaxFactor * action.getValue() > minMaxFactor * killerMoves[currentDepth][j].getValue()) {
							killerMoves[currentDepth][j + 1] = killerMoves[currentDepth][j];
						} else {
							break;
						}
					}
					killerMoves[currentDepth][j + 1] = action;
					
				}
				
				break;
			}		
		}
		
		/*
		 * se arrivo qua potrebbe essere anche perche' la mossa l'ho trovata ma con un valore piu' grande di quella che devo aggiungere
		 */
		
		if(i == killerMovesPerLevel) {
			int index;
			
			for(index=0; index<killerMovesPerLevel; index++) {
				if(minMaxFactor * action.getValue() > minMaxFactor * killerMoves[currentDepth][index].getValue()) {
					break;
				}
			}
			
			if(index < killerMovesPerLevel) {
				for(int i1=killerMovesPerLevel-2; i1>=index; i1--) {
					if(killerMoves[currentDepth][i1].getAction() != null) {
						killerMoves[currentDepth][i1 + 1] = killerMoves[currentDepth][i1];
					}
				}
				
				killerMoves[currentDepth][index] = action;
			}
		}
		
		/*
		 * not working _
		 *              |
		 *              \/
		 */
//		boolean found = false;
//		for(int i=0; i<killerMovesPerLevel; i++) {
//			if(action.getAction().equals(killerMoves[currentDepth][i].getAction())) {
//				found = true;
//				
//				if(minMaxFactor * action.getValue() > minMaxFactor * killerMoves[currentDepth][i].getValue()) {
//					killerMoves[currentDepth][i] = action;
//				}
//				
//				break;
//			}
//		}
//		
//		if(found) {
//			Arrays.sort(killerMoves[currentDepth], (x, y) -> minMaxFactor * x.getValue() == minMaxFactor * y.getValue() ? 0 : minMaxFactor * x.getValue() > minMaxFactor * y.getValue() ? 1 : -1);
//		} else {
//			if(minMaxFactor * action.getValue() > minMaxFactor * killerMoves[currentDepth][killerMovesPerLevel - 1].getValue() || killerMoves[currentDepth][killerMovesPerLevel - 1].getAction() == null) {
//				killerMoves[currentDepth][killerMovesPerLevel - 1] = action;
//				Arrays.sort(killerMoves[currentDepth], (x, y) -> minMaxFactor * x.getValue() == minMaxFactor * y.getValue() ? 0 : minMaxFactor * x.getValue() > minMaxFactor * y.getValue() ? 1 : -1);
//			}
//		}
		
	}
	
	private void printKillerMoves() {
		for (int i=0; i<killerMoves.length; i++) {
			System.out.println("Level " + i + ":");
			for (int j=0; j<killerMoves[i].length; j++) {
				System.out.println(killerMoves[i][j]);
			}
			System.out.println();
		}
	}
}
