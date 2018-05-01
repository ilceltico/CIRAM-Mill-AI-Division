package utilities;

import java.util.LinkedHashMap;

import everythingelse.Action;
import everythingelse.FromAndToAreEqualsException;
import everythingelse.FromAndToAreNotConnectedException;
import everythingelse.NoMoreCheckersAvailableException;
import everythingelse.NullActionException;
import everythingelse.NullCheckerException;
import everythingelse.NullStateException;
import everythingelse.Phase1;
import everythingelse.Phase1Action;
import everythingelse.Phase2;
import everythingelse.Phase2Action;
import everythingelse.PhaseFinal;
import everythingelse.PhaseFinalAction;
import everythingelse.PositionNotEmptyException;
import everythingelse.State;
import everythingelse.TryingToMoveOpponentCheckerException;
import everythingelse.TryingToRemoveCheckerInTripleException;
import everythingelse.TryingToRemoveEmptyCheckerException;
import everythingelse.TryingToRemoveOwnCheckerException;
import everythingelse.WrongPhaseException;
import everythingelse.WrongPositionException;
import everythingelse.State.Checker;
import everythingelse.State.Phase;
import utilities.Util;

public class Successors {
	public static LinkedHashMap<Action, State> successors(State state, Checker p) throws Exception {
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

	public static LinkedHashMap<Action, State> successorsFirst(State state, Checker p) {
		LinkedHashMap<Action, State> result = new LinkedHashMap<Action, State>();
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
								result.put(temp, newState);
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
									result.put(temp, newState);
									temp = new Phase1Action();
									temp.setPutPosition(position);
									expandedStates++;
								}
							}
						}
					} else {
						newState = Phase1.applyMove(state, temp, p);
						result.put(temp, newState);
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
				}
			}
		}

		return result;
	}

	public static LinkedHashMap<Action, State> successorsSecond(State state, Checker p) {
		LinkedHashMap<Action, State> result = new LinkedHashMap<Action, State>();
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
											result.put(temp, newState);
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
												result.put(temp, newState);
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
									result.put(temp, newState);
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
							}

						}
					}
				} catch (WrongPositionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static LinkedHashMap<Action, State> successorsFinalOrSecond(State state, Checker p) {
		if (p == Checker.WHITE) {
			if (state.getWhiteCheckersOnBoard() > 3) {
				LinkedHashMap<Action, State> resultMap = new LinkedHashMap<>();
				successorsSecond(state, p).forEach((k, v) -> {
					Phase2Action action = (Phase2Action) k;
					PhaseFinalAction result = new PhaseFinalAction();
					result.setFrom(action.getFrom());
					result.setTo(action.getTo());
					result.setRemoveOpponentChecker(action.getRemoveOpponentChecker());
					resultMap.put(result, v);
				});
				return resultMap;
			} else {
				return successorsFinal(state, p);
			}
		}
		// Player is BLACK
		else {
			if (state.getBlackCheckersOnBoard() > 3) {
				LinkedHashMap<Action, State> resultMap = new LinkedHashMap<>();
				successorsSecond(state, p).forEach((k, v) -> {
					Phase2Action action = (Phase2Action) k;
					PhaseFinalAction result = new PhaseFinalAction();
					result.setFrom(action.getFrom());
					result.setTo(action.getTo());
					result.setRemoveOpponentChecker(action.getRemoveOpponentChecker());
					resultMap.put(result, v);
				});
				return resultMap;
			} else {
				return successorsFinal(state, p);
			}
		}
	}

	public static LinkedHashMap<Action, State> successorsFinal(State state, Checker p) {
		LinkedHashMap<Action, State> result = new LinkedHashMap<Action, State>();
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
										result.put(temp, newState);
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
											result.put(temp, newState);
											temp = new PhaseFinalAction();
											temp.setFrom(position);
											temp.setTo(toPos);
											expandedStates++;
										}
									}
								}
							} else {
								newState = PhaseFinal.applyMove(state, temp, p);
								result.put(temp, newState);
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
						}

					}
				}
			}
		}

		return result;
	}

}
