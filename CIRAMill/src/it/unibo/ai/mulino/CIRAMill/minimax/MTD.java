package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

public class MTD implements IMinimax{
	
	private int expandedStates = 0;
	private int times = 0;
	private long elapsedTime;
//	private int killerArrayHits = 0;
//	private int killerMovesPerLevel;
//	private ValuedAction[][] killerMoves;
	private ITieChecker tieChecker;
	private int firstGuess;
	
	public MTD(ITieChecker tieChecker, int firstGuess) {
		this.tieChecker = tieChecker;
		this.firstGuess = firstGuess;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		/*	For use with AlfaBetaKillerVariant
		killerMovesPerLevel = 10;
		killerMoves = new ValuedAction[maxDepth][killerMovesPerLevel];
		
		for(int i=0; i<maxDepth; i++)
			for(int j=0; j<killerMovesPerLevel; j++)
				killerMoves[i][j] = new ValuedAction(null, i%2==0?Integer.MIN_VALUE:Integer.MAX_VALUE);
		*/
		elapsedTime = System.currentTimeMillis();
		ValuedAction valuedAction = mtdf(state, firstGuess, maxDepth);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("MTD-f:");
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Number of Alpha-Beta iterations: " + times);
		System.out.println("First guess: " + firstGuess);
//		System.out.println("Killer hits: " + killerArrayHits);
		System.out.println("Selected action is: " + valuedAction);
		expandedStates = 0;
		return valuedAction;
	}
	
	private ValuedAction mtdf(IState state, int firstGuess, int maxDepth) {
		ValuedAction result = new ValuedAction(null, firstGuess);
		int beta;
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;
		do {
			times++;
			
			if(result.getValue() == lowerBound)
				beta = result.getValue() + 1;
			else
				beta = result.getValue();
//			result = max(state, maxDepth, 0, beta-1, beta);	// For use with AlphaBetaKillerVariant
			result = max(state, maxDepth, beta-1, beta);
			if(result.getValue() < beta)
				upperBound = result.getValue();
			else
				lowerBound = result.getValue();
			
		} while(lowerBound < upperBound);
		
		return result;
	}
	/*	AlfaBeta	*/
	private ValuedAction max(IState state, int maxDepth, int alpha, int beta) {
		List<IAction> actions = state.getFollowingMoves();
		ValuedAction result = new ValuedAction(null, Integer.MIN_VALUE);
		ValuedAction temp = new ValuedAction();

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
//				state.unmove(a);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}

			if (temp.getValue() > result.getValue()) {
				result.set(a, temp.getValue());
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

	private ValuedAction min(IState state, int maxDepth, int alpha, int beta) {
		List<IAction> actions = state.getFollowingMoves();
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();

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
//				state.unmove(a);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
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
	
	/*	AlfaBetaKillerVariant	
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
				temp.set(a, -state.getHeuristicEvaluation());
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
	*/
}
