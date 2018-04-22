package it.unibo.ai.didattica.mulino.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.FromAndToAreEqualsException;
import it.unibo.ai.didattica.mulino.actions.FromAndToAreNotConnectedException;
import it.unibo.ai.didattica.mulino.actions.NoMoreCheckersAvailableException;
import it.unibo.ai.didattica.mulino.actions.NullActionException;
import it.unibo.ai.didattica.mulino.actions.NullCheckerException;
import it.unibo.ai.didattica.mulino.actions.NullStateException;
import it.unibo.ai.didattica.mulino.actions.Phase1;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Phase2;
import it.unibo.ai.didattica.mulino.actions.Phase2Action;
import it.unibo.ai.didattica.mulino.actions.PhaseFinal;
import it.unibo.ai.didattica.mulino.actions.PhaseFinalAction;
import it.unibo.ai.didattica.mulino.actions.PositionNotEmptyException;
import it.unibo.ai.didattica.mulino.actions.TryingToMoveOpponentCheckerException;
import it.unibo.ai.didattica.mulino.actions.TryingToRemoveCheckerInTripleException;
import it.unibo.ai.didattica.mulino.actions.TryingToRemoveEmptyCheckerException;
import it.unibo.ai.didattica.mulino.actions.TryingToRemoveOwnCheckerException;
import it.unibo.ai.didattica.mulino.actions.Util;
import it.unibo.ai.didattica.mulino.actions.WrongPhaseException;
import it.unibo.ai.didattica.mulino.actions.WrongPositionException;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;
import it.unibo.ai.didattica.mulino.domain.State.Phase;
import it.unibo.ai.didattica.mulino.domain.ValuedAction;
import it.unibo.ai.didattica.mulino.domain.ValuedAction2;

public class MulinoClientFirstMiniMaxAlphaBeta2List extends MulinoClient {

	public MulinoClientFirstMiniMaxAlphaBeta2List(Checker player) throws UnknownHostException, IOException {
		super(player);
		otherPlayer = player == Checker.WHITE ? Checker.BLACK : Checker.WHITE;
	}

	public static Checker player;
	public static Checker otherPlayer;
	private static int expandedStates;
	private static long elapsedTime;
	public static final int MAXDEPTH = 1;
	// public static final int THREAD_NUMBER = 4;
	public static List<State> statesAlreadySeen = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		MulinoClient mulinoClient = null;
		if (args[0].toLowerCase().equals("white"))
			mulinoClient = new MulinoClientFirstMiniMax(Checker.WHITE);
		else if (args[0].toLowerCase().equals("black"))
			mulinoClient = new MulinoClientFirstMiniMax(Checker.BLACK);
		else
			System.exit(-2);
		player = mulinoClient.getPlayer();
		State currentState = null;

		do {
			expandedStates = 0;

			if (player == Checker.WHITE) {
				currentState = mulinoClient.read();
				System.out.println("Current state is:");
				System.out.println(currentState.toString());

				Action a = minimaxDecision(currentState, MAXDEPTH);

				mulinoClient.write(a);

				currentState = mulinoClient.read();
				System.out.println("New state is:");
				System.out.println(currentState.toString());

				System.out.println("\nWaiting for opponent move...");

			} else if (player == Checker.BLACK) {
				currentState = mulinoClient.read();
				System.out.println("New state is:");
				System.out.println(currentState.toString());

				System.out.println("\nWaiting for opponent move...");

				currentState = mulinoClient.read();
				System.out.println("Current state is:");
				System.out.println(currentState.toString());

				Action a = minimaxDecision(currentState, MAXDEPTH);

				mulinoClient.write(a);
			} else {
				System.out.println("Wrong checker");
				System.exit(-1);
			}

		} while (currentState.getBlackCheckers() > 3 && currentState.getWhiteCheckers() > 3);

	}

	public static Action minimaxDecision(State state, int maxDepth) throws Exception {
		elapsedTime = System.currentTimeMillis();
		ValuedAction2 a = max(state, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Selected action is: " + a);
		expandedStates = 0;
		return a.getAction();
	}

	public static ValuedAction2 max(State state, int maxDepth, int alpha, int beta) throws Exception {
		List<ValuedAction2> successors = successors(state, player);
		ValuedAction2 result = new ValuedAction2(null, Integer.MIN_VALUE, null);
		ValuedAction2 temp;
		State newState;

		for (ValuedAction2 a : successors) {
			newState = a.getNewState();
			
			if (isWinningState(newState, player)) {
				result = new ValuedAction2(a.getAction(), Integer.MAX_VALUE - 1, newState);
				return result;
			}
			if (statesAlreadySeen.contains(newState)) {
				temp = new ValuedAction2(a.getAction(), 0, newState);
			} else if (maxDepth > 1) {
				statesAlreadySeen.add(newState);
				temp = min(newState, maxDepth - 1, alpha, beta);
				statesAlreadySeen.remove(newState);
			} else {
//				switch (state.getCurrentPhase()) {
//				case FIRST:
//					Phase1Action action1 = (Phase1Action) a;
////					temp = new ValuedAction(a, heuristic(newState, action1.getPutPosition(), player));
//					temp = new ValuedAction2(a.getAction(), a.getValue(), newState);
//					break;
//				case SECOND:
//					Phase2Action action2 = (Phase2Action) a;
//					temp = new ValuedAction(a, heuristic(newState, action2.getTo(), player));
//					break;
//				case FINAL:
//					PhaseFinalAction actionFinal = (PhaseFinalAction) a;
//					temp = new ValuedAction(a, heuristic(newState, actionFinal.getTo(), player));
//					break;
//				default:
//					throw new Exception("Illegal Phase");
//				}
				temp = new ValuedAction2(a.getAction(), a.getValue(), newState);
			}
			if (temp.getValue() > result.getValue()) {
				result = new ValuedAction2(a.getAction(), temp.getValue(), a.getNewState());
			}
			if (result.getValue() >= beta) {
				return result;
			}
			if (result.getValue() >= alpha) {
				alpha = result.getValue();
			}
		}
		return result;
	}

	public static ValuedAction2 min(State state, int maxDepth, int alpha, int beta) throws Exception {
		Checker minPlayer = player == Checker.BLACK ? Checker.WHITE : Checker.BLACK;
		List<ValuedAction2> successors = successors(state, minPlayer);
		ValuedAction2 result = new ValuedAction2(null, Integer.MAX_VALUE, null);
		ValuedAction2 temp;
		State newState;
		for (ValuedAction2 a : successors) {
			newState = a.getNewState();
			
			if (isWinningState(newState, minPlayer)) {
				result = new ValuedAction2(a.getAction(), Integer.MIN_VALUE + 1, newState);
				return result;
			}
			if (statesAlreadySeen.contains(newState)) {
				temp = new ValuedAction2(a.getAction(), 0, newState);
			} else if (maxDepth > 1) {
				statesAlreadySeen.add(newState);
				temp = max(newState, maxDepth - 1, alpha, beta);
				statesAlreadySeen.remove(newState);
			} else {
//				switch (state.getCurrentPhase()) {
//				case FIRST:
//					Phase1Action action1 = (Phase1Action) a;
//					temp = new ValuedAction(a, heuristic(newState, action1.getPutPosition(), minPlayer));
//					break;
//				case SECOND:
//					Phase2Action action2 = (Phase2Action) a;
//					temp = new ValuedAction(a, heuristic(newState, action2.getTo(), minPlayer));
//					break;
//				case FINAL:
//					PhaseFinalAction actionFinal = (PhaseFinalAction) a;
//					temp = new ValuedAction(a, heuristic(newState, actionFinal.getTo(), minPlayer));
//					break;
//				default:
//					throw new Exception("Illegal Phase");
//				}
				temp = new ValuedAction2(a.getAction(), a.getValue(), newState);
			}
			if (temp.getValue() < result.getValue()) {
				result = new ValuedAction2(a.getAction(), temp.getValue(), newState);
			}
			if (result.getValue() <= alpha) {
				return result;
			}
			if (result.getValue() <= beta) {
				beta = result.getValue();
			}
		}
		return result;
	}

	public static List<ValuedAction2> successors(State state, Checker p) throws Exception {
		switch (state.getCurrentPhase()) {
		case FIRST:
			return successorsFirst(state, p);
		case SECOND:
			return successorsSecond(state, p);
		case FINAL:
			return successorsFinalOrSecond(state, p);
		default:
			throw new Exception("Illegal Phase");
		}
	}

	public static List<ValuedAction2> successorsFirst(State state, Checker p) {
		// LinkedHashMap<Action, State> result = new LinkedHashMap<Action, State>();
		List<ValuedAction2> result = new ArrayList<>();
		ValuedAction2 valuedActionTemp;
		Phase1Action temp;
		State newState;
		LinkedHashMap<String, Checker> board = new LinkedHashMap<String, Checker>(state.getBoard());
		State.Checker otherChecker = p == Checker.WHITE ? Checker.BLACK : Checker.WHITE;

		for (String position : state.positions) {
			if (board.get(position) == State.Checker.EMPTY) {
				temp = new Phase1Action();
				temp.setPutPosition(position);
				newState = state.clone();
				newState.getBoard().put(position, p);
				try {
					if (Util.hasCompletedTriple(newState, position, p)) {
						boolean foundRemovableChecker = false;
						for (String otherPosition : state.positions) {
							if (board.get(otherPosition) == otherChecker
									&& !Util.hasCompletedTriple(newState, otherPosition, otherChecker)) {
								temp.setRemoveOpponentChecker(otherPosition);
								newState = Phase1.applyMove(state, temp, p);

								valuedActionTemp = new ValuedAction2(temp,
										heuristic(newState, temp.getPutPosition(), p), newState);
								result.add(valuedActionTemp);

								temp = new Phase1Action();
								temp.setPutPosition(position);
								
								expandedStates++;
								foundRemovableChecker = true;
							}
						}
						if (!foundRemovableChecker) {
							for (String otherPosition : state.positions) {
								if (board.get(otherPosition) == otherChecker
										&& Util.hasCompletedTriple(newState, otherPosition, otherChecker)) {
									temp.setRemoveOpponentChecker(otherPosition);
									newState = Phase1.applyMove(state, temp, p);

									valuedActionTemp = new ValuedAction2(temp,
											heuristic(newState, temp.getPutPosition(), p), newState);
									result.add(valuedActionTemp);
									
									temp = new Phase1Action();
									temp.setPutPosition(position);

									expandedStates++;
								}
							}
						}
					} else {
						newState = Phase1.applyMove(state, temp, p);

						valuedActionTemp = new ValuedAction2(temp, heuristic(newState, temp.getPutPosition(), p),
								newState);
						result.add(valuedActionTemp);
						
						temp = new Phase1Action();
						temp.setPutPosition(position);

						expandedStates++;
					}
				} catch (WrongPhaseException | PositionNotEmptyException | NullCheckerException
						| NoMoreCheckersAvailableException | WrongPositionException | TryingToRemoveOwnCheckerException
						| TryingToRemoveEmptyCheckerException | NullStateException
						| TryingToRemoveCheckerInTripleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		Collections.sort(result, (a, b) -> (a.getValue() == b.getValue() ? 0 : a.getValue() > b.getValue() ? 1 : -1));

		return result;
	}

	public static List<ValuedAction2> successorsSecond(State state, Checker p) {
		// LinkedHashMap<Action, State> result = new LinkedHashMap<Action, State>();
		List<ValuedAction2> result = new ArrayList<>();
		ValuedAction2 valuedActionTemp;
		Phase2Action temp;
		State newState;
		LinkedHashMap<String, Checker> board = new LinkedHashMap<String, Checker>(state.getBoard());
		State.Checker otherChecker = p == Checker.WHITE ? Checker.BLACK : Checker.WHITE;

		for (String position : state.positions) {
			if (board.get(position) == p) {
				temp = new Phase2Action();
				temp.setFrom(position);
				try {
					for (String adjPos : Util.getAdiacentTiles(position)) {
						if (board.get(adjPos) == Checker.EMPTY) {

							temp.setTo(adjPos);
							temp.setRemoveOpponentChecker(null);
							newState = state.clone();
							newState.getBoard().put(adjPos, p);
							newState.getBoard().put(position, Checker.EMPTY);
							try {
								if (Util.hasCompletedTriple(newState, adjPos, p)) {
									boolean foundRemovableChecker = false;
									for (String otherPosition : state.positions) {
										if (board.get(otherPosition) == otherChecker
												&& !Util.hasCompletedTriple(newState, otherPosition, otherChecker)) {
											temp.setRemoveOpponentChecker(otherPosition);
											if (state.getCurrentPhase() == Phase.SECOND)
												newState = Phase2.applyMove(state, temp, p);
											else {
												PhaseFinalAction finalAction = new PhaseFinalAction();
												finalAction.setFrom(temp.getFrom());
												finalAction.setTo(temp.getTo());
												finalAction.setRemoveOpponentChecker(temp.getRemoveOpponentChecker());
												newState = PhaseFinal.applyMove(state, finalAction, p);
											}

											valuedActionTemp = new ValuedAction2(temp,
													heuristic(newState, temp.getTo(), p), newState);
											result.add(valuedActionTemp);
											
											temp = new Phase2Action();
											temp.setFrom(position);
											temp.setTo(adjPos);

											expandedStates++;
											foundRemovableChecker = true;
										}
									}
									if (!foundRemovableChecker) {
										for (String otherPosition : state.positions) {
											if (board.get(otherPosition) == otherChecker
													&& Util.hasCompletedTriple(newState, otherPosition, otherChecker)) {
												temp.setRemoveOpponentChecker(otherPosition);
												if (state.getCurrentPhase() == Phase.SECOND)
													newState = Phase2.applyMove(state, temp, p);
												else {
													PhaseFinalAction finalAction = new PhaseFinalAction();
													finalAction.setFrom(temp.getFrom());
													finalAction.setTo(temp.getTo());
													finalAction
															.setRemoveOpponentChecker(temp.getRemoveOpponentChecker());
													newState = PhaseFinal.applyMove(state, finalAction, p);
												}

												valuedActionTemp = new ValuedAction2(temp,
														heuristic(newState, temp.getTo(), p), newState);
												result.add(valuedActionTemp);
												
												temp = new Phase2Action();
												temp.setFrom(position);
												temp.setTo(adjPos);

												expandedStates++;
											}
										}
									}
								} else {
									if (state.getCurrentPhase() == Phase.SECOND)
										newState = Phase2.applyMove(state, temp, p);
									else {
										PhaseFinalAction finalAction = new PhaseFinalAction();
										finalAction.setFrom(temp.getFrom());
										finalAction.setTo(temp.getTo());
										finalAction.setRemoveOpponentChecker(temp.getRemoveOpponentChecker());
										newState = PhaseFinal.applyMove(state, finalAction, p);
									}

									valuedActionTemp = new ValuedAction2(temp, heuristic(newState, temp.getTo(), p),
											newState);
									result.add(valuedActionTemp);
									
									temp = new Phase2Action();
									temp.setFrom(position);

									expandedStates++;
								}
							} catch (WrongPhaseException | PositionNotEmptyException | NullCheckerException
									| WrongPositionException | TryingToRemoveOwnCheckerException
									| TryingToRemoveEmptyCheckerException | NullStateException
									| TryingToRemoveCheckerInTripleException | NullActionException
									| TryingToMoveOpponentCheckerException | FromAndToAreEqualsException
									| FromAndToAreNotConnectedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				} catch (WrongPositionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		Collections.sort(result, (a, b) -> (a.getValue() == b.getValue() ? 0 : a.getValue() > b.getValue() ? 1 : -1));

		return result;
	}

	public static List<ValuedAction2> successorsFinalOrSecond(State state, Checker p) {
		if (p == Checker.WHITE) {
			if (state.getWhiteCheckersOnBoard() > 3) {
				// LinkedHashMap<Action, State> resultMap = new LinkedHashMap<>();
				// successorsSecond(state, p).forEach((k, v) -> {
				// Phase2Action action = (Phase2Action) k;
				// PhaseFinalAction result = new PhaseFinalAction();
				// result.setFrom(action.getFrom());
				// result.setTo(action.getTo());
				// result.setRemoveOpponentChecker(action.getRemoveOpponentChecker());
				// resultMap.put(result, v);
				// });
				// return resultMap;
				
				List<ValuedAction2> resultList = new ArrayList<>();
				
				successorsSecond(state, p).forEach(v ->{
					Phase2Action action = (Phase2Action) v.getAction();
					PhaseFinalAction result = new PhaseFinalAction();
					result.setFrom(action.getFrom());
					result.setTo(action.getTo());
					result.setRemoveOpponentChecker(action.getRemoveOpponentChecker());
					resultList.add(new ValuedAction2(result, v.getValue(), v.getNewState()));
				});			

//				return successorsSecond(state, p);
				return resultList;
			} else {
				return successorsFinal(state, p);
			}
		}
		// Player is BLACK
		else {
			if (state.getBlackCheckersOnBoard() > 3) {
				// LinkedHashMap<Action, State> resultMap = new LinkedHashMap<>();
				// successorsSecond(state, p).forEach((k, v) -> {
				// Phase2Action action = (Phase2Action) k;
				// PhaseFinalAction result = new PhaseFinalAction();
				// result.setFrom(action.getFrom());
				// result.setTo(action.getTo());
				// result.setRemoveOpponentChecker(action.getRemoveOpponentChecker());
				// resultMap.put(result, v);
				// });
				// return resultMap;
				
				List<ValuedAction2> resultList = new ArrayList<>();
				
				successorsSecond(state, p).forEach(v ->{
					Phase2Action action = (Phase2Action) v.getAction();
					PhaseFinalAction result = new PhaseFinalAction();
					result.setFrom(action.getFrom());
					result.setTo(action.getTo());
					result.setRemoveOpponentChecker(action.getRemoveOpponentChecker());
					resultList.add(new ValuedAction2(result, v.getValue(), v.getNewState()));
				});	

//				return successorsSecond(state, p);
				return resultList;
			} else {
				return successorsFinal(state, p);
			}
		}
	}

	public static List<ValuedAction2> successorsFinal(State state, Checker p) {
		// LinkedHashMap<Action, State> result = new LinkedHashMap<Action, State>();
		List<ValuedAction2> result = new ArrayList<>();
		ValuedAction2 valuedActionTemp;
		PhaseFinalAction temp;
		State newState;
		LinkedHashMap<String, Checker> board = new LinkedHashMap<String, Checker>(state.getBoard());
		State.Checker otherChecker = p == Checker.WHITE ? Checker.BLACK : Checker.WHITE;

		for (String position : state.positions) {
			if (board.get(position) == p) {
				temp = new PhaseFinalAction();
				temp.setFrom(position);
				for (String toPos : state.positions) {
					if (board.get(toPos) == Checker.EMPTY) {

						temp.setTo(toPos);
						temp.setRemoveOpponentChecker(null);
						newState = state.clone();
						newState.getBoard().put(toPos, p);
						newState.getBoard().put(position, Checker.EMPTY);
						try {
							if (Util.hasCompletedTriple(newState, toPos, p)) {
								boolean foundRemovableChecker = false;
								for (String otherPosition : state.positions) {
									if (board.get(otherPosition) == otherChecker
											&& !Util.hasCompletedTriple(newState, otherPosition, otherChecker)) {
										temp.setRemoveOpponentChecker(otherPosition);
										newState = PhaseFinal.applyMove(state, temp, p);

										valuedActionTemp = new ValuedAction2(temp, heuristic(newState, temp.getTo(), p),
												newState);
										result.add(valuedActionTemp);
										
										temp = new PhaseFinalAction();
										temp.setFrom(position);
										temp.setTo(toPos);

										expandedStates++;
										foundRemovableChecker = true;
									}
								}
								if (!foundRemovableChecker) {
									for (String otherPosition : state.positions) {
										if (board.get(otherPosition) == otherChecker
												&& Util.hasCompletedTriple(newState, otherPosition, otherChecker)) {
											temp.setRemoveOpponentChecker(otherPosition);
											newState = PhaseFinal.applyMove(state, temp, p);

											valuedActionTemp = new ValuedAction2(temp,
													heuristic(newState, temp.getTo(), p), newState);
											result.add(valuedActionTemp);
											
											temp = new PhaseFinalAction();
											temp.setFrom(position);
											temp.setTo(toPos);

											expandedStates++;
										}
									}
								}
							} else {
								newState = PhaseFinal.applyMove(state, temp, p);

								valuedActionTemp = new ValuedAction2(temp, heuristic(newState, temp.getTo(), p),
										newState);
								result.add(valuedActionTemp);
								
								temp = new PhaseFinalAction();
								temp.setFrom(position);

								expandedStates++;
							}
						} catch (WrongPhaseException | PositionNotEmptyException | NullCheckerException
								| WrongPositionException | TryingToRemoveOwnCheckerException
								| TryingToRemoveEmptyCheckerException | NullStateException
								| TryingToRemoveCheckerInTripleException | NullActionException
								| TryingToMoveOpponentCheckerException | FromAndToAreEqualsException
								| FromAndToAreNotConnectedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
		}

		Collections.sort(result, (a, b) -> (a.getValue() == b.getValue() ? 0 : a.getValue() > b.getValue() ? 1 : -1));

		return result;
	}

	public static int heuristic(State state, String position, Checker p) throws Exception {
		// // controllo se vado in pareggio
		// if (statesAlreadySeen.contains(state))
		// return 0;
		// NON NECESSARIO, GIA' PRESENTE IN MIN E MAX

		switch (state.getCurrentPhase()) {
		case FIRST:
			return heuristicPhase1(state, position, p);
		case SECOND:
			return heuristicPhase2(state, position, p);
		case FINAL:
			return heuristicPhaseFinalOr2(state, position, p);
		default:
			throw new Exception("Illegal Phase");
		}
	}

	private static int heuristicPhaseFinalOr2(State state, String position, Checker p) {
		if (p == Checker.WHITE) {
			if (state.getWhiteCheckersOnBoard() > 3)
				return heuristicPhase2(state, position, p);
			else {
				return heuristicPhaseFinal(state, position, p);
			}
		}
		// Player is BLACK
		else {
			if (state.getBlackCheckersOnBoard() > 3)
				return heuristicPhase2(state, position, p);
			else {
				return heuristicPhaseFinal(state, position, p);
			}
		}
	}

	private static int heuristicPhaseFinal(State state, String position, Checker p) {
		int result = 0;

		// number of 2 pieces configuration
		int twoPiecesConfigurationPlayer = 0;
		int twoPiecesConfigurationOtherPlayer = 0;

		// number of 3 pieces configuration
		int threePiecesConfigurationPlayer = 0;
		int threePiecesConfigurationOtherPlayer = 0;

		for (String pos : state.positions) {
			// number of 2 pieces configuration
			if (state.getBoard().get(pos) == player) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == player)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2)
					twoPiecesConfigurationPlayer++;

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == player)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosV == 1 && occupiedPosV == 2)
					twoPiecesConfigurationPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == otherPlayer)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2)
					twoPiecesConfigurationOtherPlayer++;

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == otherPlayer)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosV == 1 && occupiedPosV == 2)
					twoPiecesConfigurationOtherPlayer++;
			}

			// number of 3 pieces configuration
			if (state.getBoard().get(pos) == player) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == player)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == player)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2 && emptyPosV == 1 && occupiedPosV == 2)
					threePiecesConfigurationPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == otherPlayer)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == otherPlayer)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2 && emptyPosV == 1 && occupiedPosV == 2)
					threePiecesConfigurationOtherPlayer++;
			}
		}
		result += 10 * (twoPiecesConfigurationPlayer / 2);
		result -= 10 * (twoPiecesConfigurationOtherPlayer / 2);

		result += threePiecesConfigurationPlayer;
		result -= threePiecesConfigurationOtherPlayer;

		// closed morris
		if (p == player) {
			if (Util.hasCompletedTriple(state, position, p)) {
				result += 16;
			}
		} else { // other player
			if (Util.hasCompletedTriple(state, position, p)) {
				result -= 16;
			}
		}

		return result;
	}

	private static int heuristicPhase2(State state, String position, Checker p) {
		int result = 0;

		// closed morris
		if (p == player) {
			if (Util.hasCompletedTriple(state, position, p)) {
				result += 14;
			}
		} else { // other player
			if (Util.hasCompletedTriple(state, position, p)) {
				result -= 14;
			}
		}

		// morrises number
		int morrisClosed3player = 0;
		int morrisClosed3OtherPlayer = 0;

		// number of blocked oppenent pieces
		int blockedPiecesPlayer = 0;
		int blockedPiecesOtherPlayer = 0;

		// double morris
		int doubleMorrisPlayer = 0;
		int doubleMorrisOtherPlayer = 0;
		for (String pos : state.positions) {
			// morrises number
			if (state.getBoard().get(pos) == player) {
				if (Util.hasCompletedTriple(state, pos, player)) {
					morrisClosed3player++;
				}
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				if (Util.hasCompletedTriple(state, pos, otherPlayer)) {
					morrisClosed3OtherPlayer++;
				}
			}

			// number of blocked oppenent pieces
			if (state.getBoard().get(pos) == otherPlayer) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPiecesPlayer++;
				}
			}

			if (state.getBoard().get(pos) == player) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPiecesOtherPlayer++;
				}
			}

			// double morris
			if (state.getBoard().get(pos) == player) {
				if (Util.isInHTriple(state, pos) && Util.isInVTriple(state, pos))
					doubleMorrisPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				if (Util.isInHTriple(state, pos) && Util.isInVTriple(state, pos))
					doubleMorrisOtherPlayer++;

			}
		}
		result += 43 * (morrisClosed3player / 3);
		result -= 43 * (morrisClosed3OtherPlayer / 3);

		result += 10 * blockedPiecesPlayer;
		result -= 10 * blockedPiecesOtherPlayer;

		result += 42 * doubleMorrisPlayer;
		result -= 42 * doubleMorrisOtherPlayer;

		// pieces number
		if (player == Checker.WHITE)
			result += 6 * (state.getWhiteCheckersOnBoard() - state.getBlackCheckersOnBoard());
		else
			result += 6 * (state.getBlackCheckersOnBoard() - state.getWhiteCheckersOnBoard());

		// opened morris (che cazzo �???)

		return result;
	}

	private static int heuristicPhase1(State state, String position, Checker p) {
		int result = 0;

		// closed morris
		if (p == player) {
			if (Util.hasCompletedTriple(state, position, p)) {
				result += 18;
			}
		} else { // other player
			if (Util.hasCompletedTriple(state, position, p)) {
				result -= 18;
			}
		}

		// morrises number
		int morrisClosed3player = 0;
		int morrisClosed3OtherPlayer = 0;

		// number of blocked oppenent pieces
		int blockedPiecesPlayer = 0;
		int blockedPiecesOtherPlayer = 0;

		// number of 2 pieces configuration
		int twoPiecesConfigurationPlayer = 0;
		int twoPiecesConfigurationOtherPlayer = 0;

		// number of 3 pieces configuration
		int threePiecesConfigurationPlayer = 0;
		int threePiecesConfigurationOtherPlayer = 0;
		for (String pos : state.positions) {
			// morrises number
			if (state.getBoard().get(pos) == player) {
				if (Util.hasCompletedTriple(state, pos, player)) {
					morrisClosed3player++;
				}
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				if (Util.hasCompletedTriple(state, pos, otherPlayer)) {
					morrisClosed3OtherPlayer++;
				}
			}

			// number of blocked oppenent pieces
			if (state.getBoard().get(pos) == otherPlayer) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPiecesPlayer++;
				}
			}

			if (state.getBoard().get(pos) == player) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPiecesOtherPlayer++;
				}
			}

			// number of 2 pieces configuration
			if (state.getBoard().get(pos) == player) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == player)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2)
					twoPiecesConfigurationPlayer++;

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == player)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosV == 1 && occupiedPosV == 2)
					twoPiecesConfigurationPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == otherPlayer)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2)
					twoPiecesConfigurationOtherPlayer++;

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == otherPlayer)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosV == 1 && occupiedPosV == 2)
					twoPiecesConfigurationOtherPlayer++;
			}

			// number of 3 pieces configuration
			if (state.getBoard().get(pos) == player) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == player)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == player)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2 && emptyPosV == 1 && occupiedPosV == 2)
					threePiecesConfigurationPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == otherPlayer)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == otherPlayer)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2 && emptyPosV == 1 && occupiedPosV == 2)
					threePiecesConfigurationOtherPlayer++;
			}
		}
		result += 26 * (morrisClosed3player / 3);
		result -= 26 * (morrisClosed3OtherPlayer / 3);

		result += blockedPiecesPlayer;
		result -= blockedPiecesOtherPlayer;

		result += 12 * (twoPiecesConfigurationPlayer / 2);
		result -= 12 * (twoPiecesConfigurationOtherPlayer / 2);

		result += 7 * threePiecesConfigurationPlayer;
		result -= 7 * threePiecesConfigurationOtherPlayer;

		// pieces number
		if (player == Checker.WHITE)
			result += 6 * (state.getWhiteCheckersOnBoard() - state.getBlackCheckersOnBoard());
		else
			result += 6 * (state.getBlackCheckersOnBoard() - state.getWhiteCheckersOnBoard());

		return result;
	}

	public static boolean isWinningState(State state, Checker p) {
		if (state.getCurrentPhase() == State.Phase.FIRST)
			return false;
		if (p == Checker.WHITE && state.getBlackCheckersOnBoard() < 3)
			return true;
		else if (p == Checker.BLACK && state.getWhiteCheckersOnBoard() < 3)
			return true;

		if (state.getCurrentPhase() == State.Phase.SECOND) {
			Checker otherPlayer = p == Checker.WHITE ? Checker.BLACK : Checker.WHITE;
			for (String position : state.positions) {
				if (state.getBoard().get(position) == otherPlayer) {
					boolean isBlocked = true;
					try {
						for (String adjPos : Util.getAdiacentTiles(position)) {
							if (state.getBoard().get(adjPos) == Checker.EMPTY) {
								isBlocked = false;
								break;
							}
						}
					} catch (WrongPositionException e) {
						e.printStackTrace();
					}
					if (!isBlocked) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

}
