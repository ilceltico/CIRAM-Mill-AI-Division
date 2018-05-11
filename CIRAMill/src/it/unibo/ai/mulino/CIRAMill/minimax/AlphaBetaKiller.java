package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.Arrays;
import java.util.List;

public class AlphaBetaKiller implements IMinimax {
	private int expandedStates = 0;
	private long elapsedTime;
	private ITieChecker tieChecker;
	private int killerMovesForLevel;
	private ValuedAction[][] killerMoves;

	public AlphaBetaKiller(ITieChecker tieChecker, int killerMovesForLevel) {
		this.tieChecker = tieChecker;
		this.killerMovesForLevel = killerMovesForLevel;
	}

	@Override
	public ValuedAction minimaxDecision(IState state, int maxDepth) {
		killerMoves = new ValuedAction[maxDepth][killerMovesForLevel];
		
		for(int i=0; i<maxDepth; i++)
			for(int j=0; j<killerMovesForLevel; j++)
				killerMoves[i][j] = new ValuedAction();
		
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
		
		for(int i=0; i<killerMovesForLevel; i++) {
			killer = killerMoves[currentDepth][i];
			
			if(killer.getAction() != null && state.isLegalMove(killer.getAction())) {
				state.move(killer.getAction());				
				if (state.isWinningState()) {
					result.set(killer.getAction(), Integer.MAX_VALUE-1);
					state.unmove(killer.getAction());
					break;
				} else if (tieChecker.isTie(state)) {
					temp.set(killer.getAction(), 0);
				} else if (maxDepth > 1) {
//					state.move(a);
					temp = min(state, maxDepth - 1, currentDepth + 1, alpha, beta);
//					state.unmove(a);
				} else {
					result.set(killer.getAction(), -state.getHeuristicEvaluation());
				}
				
				if (temp.getValue() > result.getValue()) {
					result.set(killer.getAction(), temp.getValue());
					/*
					 * aggiornamento array?
					 */
					
					int index = -1;
					
					for(int j=0; j<killerMovesForLevel; j++) {
						if(killerMoves[currentDepth][j].getAction() == null || result.getValue() > killerMoves[currentDepth][j].getValue()) {
							index = j;
							break;
						}
					}
					
					if(index >= 0) {
						for(int j=killerMovesForLevel-2; j>=index; j--) {
							if(killerMoves[currentDepth][j].getAction() != null)
								killerMoves[currentDepth][j + 1] = killerMoves[currentDepth][j];
						}
						
						killerMoves[currentDepth][index] = result;
					}
					
					// controllare che un'azione non ci sia gia'
				}
				if (result.getValue() >= alpha) {
					state.unmove(killer.getAction());
					return result;
				}
				if(result.getValue() >= beta) {
					beta = result.getValue();
				}
				
				state.unmove(killer.getAction());
			}
		}
		
		List<IAction> actions = state.getFollowingMoves();
		
		for(int i=0; i<killerMovesForLevel; i++) {
			if(killerMoves[currentDepth][i].getAction() != null)
				actions.remove(killerMoves[currentDepth][i].getAction());
		}
		
		for (IAction a : actions) {
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
				temp = min(state, maxDepth - 1, currentDepth + 1, alpha, beta);
//				state.unmove(a);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			
			if (temp.getValue() > result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() >= beta) {
				state.unmove(a);
				
				// per ora aggiungo in fondo e sorto
				
				// dovrei mettere ValuedAction.value a Int.minVal o maxVal?? il problema e' nella sort quando ho anche valori negativi
				// e dipende anche dal fatto che sono in min e max!!!!!!!!!!!
				// risolvo forse dicendo di non considerare le ValAction con action nulla, come faccio?
				// o forse a me non importa un cazzo se io chiamo minimax per me o l'avversario, indice pari e' max quindi inizialmete
				// value = minvalue e indice dispari al contrario
				
				// se uso il metodo di inserimento risolvo
				
				// controllare che un'azione non ci sia gia'
				
				// devo istanziare nuovamente result anche se faccio return?
//				if(killerMoves[currentDepth][killerMovesForLevel - 1].getAction() == null)
//					killerMoves[currentDepth][killerMovesForLevel - 1] = result;
//				else if(result.getValue() > killerMoves[currentDepth][killerMovesForLevel - 1].getValue())
//					killerMoves[currentDepth][killerMovesForLevel - 1] = result;
				
//				Arrays.sort(killerMoves[currentDepth], (x, y) -> (x.getValue() == y.getValue() ? 0 : x.getValue() > y.getValue() ? 1 : -1));
				
//				Arrays.sort(killerMoves[currentDepth], (x, y) -> (Math.max(x.getValue(), y.getValue()));
				
//				killerMoves[currentDepth] = (ValuedAction[]) Arrays.stream(killerMoves[currentDepth]).filter(x -> x.getAction() != null).sorted((x, y) -> Math.max(x.getValue(), y.getValue())).toArray();
				
				int index = -1;
				
				for(int i=0; i<killerMovesForLevel; i++) {
					if(killerMoves[currentDepth][i].getAction() == null || result.getValue() > killerMoves[currentDepth][i].getValue()) {
						index = i;
						break;
					}
				}
				
				if(index >= 0) {
					for(int i=killerMovesForLevel-2; i>=index; i--) {
						if(killerMoves[currentDepth][i].getAction() != null)
							killerMoves[currentDepth][i + 1] = killerMoves[currentDepth][i];
					}
					
					killerMoves[currentDepth][index] = result;
				}
				
				return result;
			}
			if(result.getValue() >= alpha) {
				alpha = result.getValue();
			}
			
			state.unmove(a);
		}
		
		/*
		 * aggiornamento array
		 */
		
//		if(result.getValue() > killerMoves[currentDepth][killerMovesForLevel - 1].getValue() && result.getValue() < killerMoves[currentDepth][killerMovesForLevel - 2].getValue()) {
//			killerMoves[currentDepth][killerMovesForLevel - 1] = result;
//		} else if(result.getValue() > killerMoves[currentDepth][killerMovesForLevel - 2].getValue()) {
//		
//			for(int i=killerMovesForLevel-2; i>=0; i--) {
//				if(result.getValue() < killerMoves[currentDepth][i].getValue() && result.getValue() > killerMoves[currentDepth][i + 1].getValue()) {
//					killerMoves[currentDepth][i + 1] = result;
//				} else if(result.getValue() > killerMoves[currentDepth][i].getValue()) {
//					killerMoves[currentDepth][i + 1] = killerMoves[currentDepth][i];
//				}
//			}
//		}
		
		addKillerMove()
		
		return result;
	}
	
	private ValuedAction min(IState state, int maxDepth, int currentDepth, int alpha, int beta) {
		ValuedAction result = new ValuedAction(null, Integer.MAX_VALUE);
		ValuedAction temp = new ValuedAction();
		
		ValuedAction killer;
		
		for(int i=0; i<killerMovesForLevel; i++) {
			killer = killerMoves[currentDepth][i];
			
			if(killer.getAction() != null && state.isLegalMove(killer.getAction())) {
				state.move(killer.getAction());				
				if (state.isWinningState()) {
					result.set(killer.getAction(), Integer.MIN_VALUE+1);
					state.unmove(killer.getAction());
					break;
				} else if (tieChecker.isTie(state)) {
					temp.set(killer.getAction(), 0);
				} else if (maxDepth > 1) {
//					state.move(a);
					temp = min(state, maxDepth - 1, currentDepth + 1, alpha, beta);
//					state.unmove(a);
				} else {
					result.set(killer.getAction(), -state.getHeuristicEvaluation());
				}
				
				if (temp.getValue() < result.getValue()) {
					result.set(killer.getAction(), temp.getValue());
					/*
					 * aggiornamento array?
					 */
					
					int index = -1;
					
					for(int j=0; j<killerMovesForLevel; j++) {
						if(killerMoves[currentDepth][j].getAction() == null || result.getValue() < killerMoves[currentDepth][j].getValue()) {
							index = j;
							break;
						}
					}
					
					if(index >= 0) {
						for(int j=killerMovesForLevel-2; j>=index; j--) {
							if(killerMoves[currentDepth][j].getAction() != null)
								killerMoves[currentDepth][j + 1] = killerMoves[currentDepth][j];
						}
						
						killerMoves[currentDepth][index] = result;
					}
					
					// controllare che un'azione non ci sia gia'
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
		
		for(int i=0; i<killerMovesForLevel; i++) {
			if(killerMoves[currentDepth][i].getAction() != null)
				actions.remove(killerMoves[currentDepth][i].getAction());
		}
		
		for (IAction a : actions) {
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
				temp = max(state, maxDepth - 1, currentDepth + 1, alpha, beta);
//				state.unmove(a);
			} else {
				temp.set(a, -state.getHeuristicEvaluation());
			}
			if (temp.getValue() < result.getValue()) {
				result.set(a, temp.getValue());
			}
			if (result.getValue() <= alpha) {
				state.unmove(a);
				
				int index = -1;
				
				for(int i=0; i<killerMovesForLevel; i++) {
					if(killerMoves[currentDepth][i].getAction() == null || result.getValue() < killerMoves[currentDepth][i].getValue()) {
						index = i;
						break;
					}
				}
				
				if(index >= 0) {
					for(int i=killerMovesForLevel-2; i>=index; i--) {
						if(killerMoves[currentDepth][i].getAction() != null)
							killerMoves[currentDepth][i + 1] = killerMoves[currentDepth][i];
					}
					
					killerMoves[currentDepth][index] = result;
				}
				
				return result;
			}
			if(result.getValue() <= beta) {
				beta = result.getValue();
			}
			
			state.unmove(a);
		}
		
		int index = -1;
		
		for(int i=0; i<killerMovesForLevel; i++) {
			if(killerMoves[currentDepth][i].getAction() == null || result.getValue() < killerMoves[currentDepth][i].getValue()) {
				index = i;
				break;
			}
		}
		
		if(index >= 0) {
			for(int i=killerMovesForLevel-2; i>=index; i--) {
				if(killerMoves[currentDepth][i].getAction() != null)
					killerMoves[currentDepth][i + 1] = killerMoves[currentDepth][i];
			}
			
			killerMoves[currentDepth][index] = result;
		}
		
		return result;
	}
	
	private void addKillerMove(ValuedAction action, int currentDepth) {
		for(int i=0; i<killerMovesForLevel; i++) {
			if(action.getAction().equals(killerMoves[currentDepth][i].getAction()) && action.getValue() > killerMoves[currentDepth][i].getValue()) {
				
				for(int j=i-1; j>=0; j--) {
					if(action.getValue() > killerMoves[currentDepth][j].getValue()) {
						killerMoves[currentDepth][j + 1] = killerMoves[currentDepth][j];
					} else {
						killerMoves[currentDepth][j + 1] = new ValuedAction(action.getAction(), action.getValue());
						break;
					}
				}
				
				break;
			}				
		}
		
		
		
		int index = -1;
		
		for(int i=0; i<killerMovesForLevel; i++) {
			if(killerMoves[currentDepth][i].getAction() == null || action.getValue() > killerMoves[currentDepth][i].getValue()) {
				index = i;
				break;
			}
		}
		
		if(index >= 0) {
			for(int i=killerMovesForLevel-2; i>=index; i--) {
				if(killerMoves[currentDepth][i].getAction() != null)
					killerMoves[currentDepth][i + 1] = killerMoves[currentDepth][i];
			}
			
			killerMoves[currentDepth][index] = action;
		}
		
		
	}
}
