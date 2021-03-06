package it.unibo.ai.didattica.mulino.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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
import it.unibo.ai.didattica.mulino.client.MulinoClientFirstMiniMax.IterativeDeepeningRunnable;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.ValuedAction;
import it.unibo.ai.didattica.mulino.domain.State.Checker;
import it.unibo.ai.didattica.mulino.domain.State.Phase;

public class MulinoClientFirstNegascout2 extends MulinoClient {

	public MulinoClientFirstNegascout2(Checker player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	public static Checker player;
	public static Checker otherPlayer;
	private static int expandedStates;
	private static long elapsedTime;
	public static final int MAXDEPTH = 1;
	// public static final int THREAD_NUMBER = 4;
	public static final int SAFETY_MILLIS = 1000;
	public static List<State> statesAlreadySeen = new ArrayList<>();
	public static Action selectedAction;

	public static void main(String[] args) throws Exception {
		MulinoClientFirstMiniMaxAlphaBeta mulinoClient = null;
		if (args.length < 1) {
			System.out.println("1 argument required, case insensitive: white | black");
			System.exit(-1);
		} else if (args[0].toLowerCase().equals("white"))
			mulinoClient = new MulinoClientFirstMiniMaxAlphaBeta(Checker.WHITE);
		else if (args[0].toLowerCase().equals("black"))
			mulinoClient = new MulinoClientFirstMiniMaxAlphaBeta(Checker.BLACK);
		else {
			System.out.println("1 argument required, case insensitive: white | black");
			System.exit(-2);
		}
		player = mulinoClient.getPlayer();
		State currentState = null;

		do {
			expandedStates = 0;

			if (player == Checker.WHITE) {
				currentState = mulinoClient.read();
				statesAlreadySeen.add(currentState);
				System.out.println("Current state is:");
				System.out.println(currentState.toString());

				Action a = mulinoClient.iterativeDeepeningMinimaxDecision(currentState, 60000);

				mulinoClient.write(a);

				currentState = mulinoClient.read();
				statesAlreadySeen.add(currentState);
				System.out.println("New state is:");
				System.out.println(currentState.toString());

				System.out.println("\nWaiting for opponent move...");

			} else if (player == Checker.BLACK) {
				currentState = mulinoClient.read();
				statesAlreadySeen.add(currentState);
				System.out.println("New state is:");
				System.out.println(currentState.toString());

				System.out.println("\nWaiting for opponent move...");

				currentState = mulinoClient.read();
				statesAlreadySeen.add(currentState);
				System.out.println("Current state is:");
				System.out.println(currentState.toString());

				Action a = mulinoClient.iterativeDeepeningMinimaxDecision(currentState, 60000);

				mulinoClient.write(a);
			} else {
				System.out.println("Wrong checker");
				System.exit(-1);
			}

		} while (true);

	}
	
//	public class IterativeDeepeningRunnable implements Runnable {
//		private State state;
//		private Action iterativeAction = null;
//		
//		public void setState(State state) {
//			this.state = state;
//		}
//		
//		public Action getIterativeAction() {
//			return iterativeAction;
//		}
//		
//		@Override
//		public void run() {
//			int iterativeLevel = 2;
//			try {
//				while(iterativeLevel<20) {
//					iterativeAction = minimaxDecision(state, iterativeLevel);
//					System.out.println("Level " + iterativeLevel++  + " decision completed");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
//	
//	@SuppressWarnings("deprecation")
//	public Action iterativeDeepeningMinimaxDecision(State state, long millisLimit) throws Exception {
//		
//		IterativeDeepeningRunnable runnable = new IterativeDeepeningRunnable();
//		runnable.setState(state);
//		List<State> statesAlreadySeenCopy = new ArrayList<State>(MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen);
//		
//		Thread thread = new Thread(runnable);
//		thread.start();
//		
//		Thread.sleep(millisLimit-SAFETY_MILLIS);
//		System.out.println("Reached time limit");
//		
//		thread.stop();
//		MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen = statesAlreadySeenCopy;
//		
//		return runnable.getIterativeAction();
//	}

	public static Action negascoutDecision(State state, int maxDepth) throws Exception {
		elapsedTime = System.currentTimeMillis();
		int a = negascout(state, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("Elapsed time: " + elapsedTime);
		System.out.println("Expanded states: " + expandedStates);
		System.out.println("Selected action is: " + selectedAction + ", value: " + a);
		expandedStates = 0;
		return selectedAction;
	}
	
	/*
	 * versione wikipedia italiano e chessprogramming.wikispaces.com
	 */
	
	public static int negascout(State state, int maxDepth, int alpha, int beta) throws Exception {
		LinkedHashMap<Action, State> successors = successors(state, player);
		int result = Integer.MIN_VALUE;
		int tempValue;
		State newState;
		
		Set<Action> successorsKeySet = successors.keySet();
		Action[] successorsKeyArray = successorsKeySet.toArray(new Action[successorsKeySet.size()]);
		
		for(int i=0; i<successorsKeyArray.length; i++) {
			Action a = successorsKeyArray[i];
			newState = successors.get(a);
			
			if (isWinningState(newState, player)) {
				selectedAction = a;
				return Integer.MAX_VALUE-1;
			}
			if (statesAlreadySeen.contains(newState)) {
				tempValue = 0;
			}
		}
	}
	
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

	public static int heuristic(State state, String position, Checker p) throws Exception {
//		// controllo se vado in pareggio
//		if (statesAlreadySeen.contains(state))
//			return 0;
		//NON NECESSARIO, GIA' PRESENTE IN MIN E MAX

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
