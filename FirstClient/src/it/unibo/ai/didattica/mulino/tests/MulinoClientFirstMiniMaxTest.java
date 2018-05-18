package it.unibo.ai.didattica.mulino.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Phase2;
import it.unibo.ai.didattica.mulino.actions.Phase2Action;
import it.unibo.ai.didattica.mulino.actions.PhaseFinal;
import it.unibo.ai.didattica.mulino.actions.PhaseFinalAction;
import it.unibo.ai.didattica.mulino.actions.WrongPhaseException;
import it.unibo.ai.didattica.mulino.client.MulinoClientFirstMiniMax;
import it.unibo.ai.didattica.mulino.client.MulinoClientFirstMiniMaxAlphaBeta;
import it.unibo.ai.didattica.mulino.client.MulinoClientFirstMiniMaxAlphaBeta2TreeMap;
import it.unibo.ai.didattica.mulino.client.MulinoClientFirstMiniMaxAlphaBetaKiller;
import it.unibo.ai.didattica.mulino.client.MulinoClientFirstNegamax;
import it.unibo.ai.didattica.mulino.client.MulinoClientFirstMiniMaxAlphaBeta2List;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;
import it.unibo.ai.didattica.mulino.domain.State.Phase;
import it.unibo.ai.didattica.mulino.domain.ValuedAction;

public class MulinoClientFirstMiniMaxTest {

	public static void main(String[] args) {
//		 doTest1();
//		doTest1WithAlphaBeta();
		// doTest2();
//		 doTest2WithAlphaBeta();
		// doTest2WithAlphaBetaAndOptimizationsTreeSet();
		// doTest2WithAlphaBetaAndOptimizationsList();
		
		// playAgaintsWhiteCPU();
//		 playAgainstBlackCPU();
//		 playAgainstBlackIterativeCPU();
//		 playAgainstBlackIterativeKillerCPU();
		 doTestStates();

//		doTestIterativeDeepening();
//		doTestIterativeDeepeningAlphaBeta();
//		doTestIterativeDeepeningKiller();
		 
//		 doTestNegamax();
	}

	public static void doTest1() {
		MulinoClientFirstMiniMax.player = Checker.WHITE;
		MulinoClientFirstMiniMax.otherPlayer = Checker.BLACK;
		State initialState = new State();

		try {
			MulinoClientFirstMiniMax.minimaxDecision(initialState, 6);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void doTest1WithAlphaBeta() {
		MulinoClientFirstMiniMaxAlphaBeta.player = Checker.WHITE;
		MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.BLACK;
		State initialState = new State();

		try {
			MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(initialState, 6);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doTest2() {
		State state = new State();
		Action a;

		try {
			for (int i = 0; i < 1000; i++) {
				MulinoClientFirstMiniMax.player = Checker.WHITE;
				MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.BLACK;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 5);

				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);

				MulinoClientFirstMiniMax.player = Checker.BLACK;
				MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.WHITE;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 1);
				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);

				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doTest2WithAlphaBeta() {
		State state = new State();
		Action a;

		try {
			for (int i = 0; i < 1000; i++) {
				MulinoClientFirstMiniMaxAlphaBeta.player = Checker.WHITE;
				MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.BLACK;
				a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 5);

				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.add(state);

				MulinoClientFirstMiniMaxAlphaBeta.player = Checker.BLACK;
				MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.WHITE;
				a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 5);
				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.add(state);

				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doTest2WithAlphaBetaAndOptimizationsTreeSet() {
		State state = new State();
		Action a;

		try {
			for (int i = 0; i < 1000; i++) {
				MulinoClientFirstMiniMaxAlphaBeta2TreeMap.player = Checker.WHITE;
				MulinoClientFirstMiniMaxAlphaBeta2TreeMap.otherPlayer = Checker.BLACK;
				a = MulinoClientFirstMiniMaxAlphaBeta2TreeMap.minimaxDecision(state, 6);

				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2TreeMap.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2TreeMap.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2TreeMap.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta2TreeMap.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta2TreeMap.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta2TreeMap.statesAlreadySeen.add(state);

				MulinoClientFirstMiniMaxAlphaBeta2TreeMap.player = Checker.BLACK;
				MulinoClientFirstMiniMaxAlphaBeta2TreeMap.otherPlayer = Checker.WHITE;
				a = MulinoClientFirstMiniMaxAlphaBeta2TreeMap.minimaxDecision(state, 6);
				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2TreeMap.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2TreeMap.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2TreeMap.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta2TreeMap.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta2TreeMap.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta2TreeMap.statesAlreadySeen.add(state);

				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doTest2WithAlphaBetaAndOptimizationsList() {
		State state = new State();
		Action a;

		try {
			for (int i = 0; i < 1000; i++) {
				MulinoClientFirstMiniMaxAlphaBeta2List.player = Checker.WHITE;
				MulinoClientFirstMiniMaxAlphaBeta2List.otherPlayer = Checker.BLACK;
				a = MulinoClientFirstMiniMaxAlphaBeta2List.minimaxDecision(state, 4);

				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2List.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2List.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2List.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta2List.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta2List.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta2List.statesAlreadySeen.add(state);

				MulinoClientFirstMiniMaxAlphaBeta2List.player = Checker.BLACK;
				MulinoClientFirstMiniMaxAlphaBeta2List.otherPlayer = Checker.WHITE;
				a = MulinoClientFirstMiniMaxAlphaBeta2List.minimaxDecision(state, 4);
				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2List.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2List.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta2List.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta2List.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta2List.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta2List.statesAlreadySeen.add(state);

				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void playAgaintsWhiteCPU() {
		State state = new State();
		Action a;

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			for (int i = 0; i < 1000; i++) {
				MulinoClientFirstMiniMax.player = Checker.WHITE;
				MulinoClientFirstMiniMax.otherPlayer = Checker.BLACK;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 4);

				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);

				System.out.println("Player BLACK, do your move: ");
				String actionString = in.readLine();
				a = stringToAction(actionString, state.getCurrentPhase());
				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, Checker.BLACK);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, Checker.BLACK);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, Checker.BLACK);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);

				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void playAgainstBlackCPU() {
		State state = new State();
		Action a;

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			for (int i = 0; i < 1000; i++) {
				System.out.println("Player WHITE, do your move: ");
				String actionString = in.readLine();
				a = stringToAction(actionString, state.getCurrentPhase());
				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, Checker.WHITE);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, Checker.WHITE);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, Checker.WHITE);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);

				MulinoClientFirstMiniMax.player = Checker.BLACK;
				MulinoClientFirstMiniMax.otherPlayer = Checker.WHITE;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 5);

				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);

				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void playAgainstBlackIterativeCPU() {
		State state = new State();
		Action a;
		MulinoClientFirstMiniMaxAlphaBeta client = null;
		try {
			client = new MulinoClientFirstMiniMaxAlphaBeta(Checker.BLACK);
		} catch (IOException e1) {
			 e1.printStackTrace();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			for (int i = 0; i < 1000; i++) {
				System.out.println("Player WHITE, do your move: ");
				String actionString = in.readLine();
				a = stringToAction(actionString, state.getCurrentPhase());
				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, Checker.WHITE);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, Checker.WHITE);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, Checker.WHITE);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.add(state);
				
				//Stampa tutti gli stati già visti
//				for (State s : MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen) {
//					System.out.println(s);
//				}

				MulinoClientFirstMiniMaxAlphaBeta.player = Checker.BLACK;
				MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.WHITE;
				
				a = client.iterativeDeepeningMinimaxDecision(state, 20000);
//				a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 5);

				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.add(state);
				//Stampa tutti gli stati già visti
//				for (State s : MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen) {
//					System.out.println(s);
//				}
				
				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void playAgainstBlackIterativeKillerCPU() {
		State state = new State();
		Action a;
		MulinoClientFirstMiniMaxAlphaBetaKiller client = null;
		try {
			client = new MulinoClientFirstMiniMaxAlphaBetaKiller(Checker.BLACK);
		} catch (IOException e1) {
			 e1.printStackTrace();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			for (int i = 0; i < 1000; i++) {
				System.out.println("Player WHITE, do your move: ");
				String actionString = in.readLine();
				a = stringToAction(actionString, state.getCurrentPhase());
				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, Checker.WHITE);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, Checker.WHITE);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, Checker.WHITE);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBetaKiller.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBetaKiller.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBetaKiller.statesAlreadySeen.add(state);
				
				//Stampa tutti gli stati già visti
//				for (State s : MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen) {
//					System.out.println(s);
//				}

				MulinoClientFirstMiniMaxAlphaBetaKiller.player = Checker.BLACK;
				MulinoClientFirstMiniMaxAlphaBetaKiller.otherPlayer = Checker.WHITE;
				
				a = client.iterativeDeepeningMinimaxDecision(state, 20000);
//				a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 5);

				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBetaKiller.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBetaKiller.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBetaKiller.statesAlreadySeen.add(state);
				//Stampa tutti gli stati già visti
//				for (State s : MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen) {
//					System.out.println(s);
//				}
				
				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Converte una stringa testuale in un oggetto azione
	 * 
	 * @param actionString
	 *            La stringa testuale che esprime l'azione desiderata
	 * @param fase
	 *            La fase di gioco attuale
	 * @return L'oggetto azione da comunicare al server
	 */
	private static Action stringToAction(String actionString, Phase fase) {
		if (fase == Phase.FIRST) { // prima fase
			Phase1Action action;
			action = new Phase1Action();
			action.setPutPosition(actionString.substring(0, 2));
			if (actionString.length() == 4)
				action.setRemoveOpponentChecker(actionString.substring(2, 4));
			else
				action.setRemoveOpponentChecker(null);
			return action;
		} else if (fase == Phase.SECOND) { // seconda fase
			Phase2Action action;
			action = new Phase2Action();
			action.setFrom(actionString.substring(0, 2));
			action.setTo(actionString.substring(2, 4));
			if (actionString.length() == 6)
				action.setRemoveOpponentChecker(actionString.substring(4, 6));
			else
				action.setRemoveOpponentChecker(null);
			return action;
		} else { // ultima fase
			PhaseFinalAction action;
			action = new PhaseFinalAction();
			action.setFrom(actionString.substring(0, 2));
			action.setTo(actionString.substring(2, 4));
			if (actionString.length() == 6)
				action.setRemoveOpponentChecker(actionString.substring(4, 6));
			else
				action.setRemoveOpponentChecker(null);
			return action;
		}
	}

	public static void doTestStates() {
		State state;
		Action a;

		// STATO 12 DELLA PARTITA 7v7 ALPHA-BETA PRIMA DELLA CORREZIONE DEI SUCCESSORI
		// try {
		// state = new State();
		// state.setBlackCheckers(0);
		// state.setWhiteCheckers(0);
		// state.setCurrentPhase(State.Phase.SECOND);
		// state.setBlackCheckersOnBoard(5);
		// state.setWhiteCheckersOnBoard(7);
		// state.getBoard().put("b6", Checker.BLACK);
		// state.getBoard().put("d6", Checker.BLACK);
		// state.getBoard().put("f6", Checker.BLACK);
		// state.getBoard().put("g4", Checker.BLACK);
		// state.getBoard().put("f2", Checker.BLACK);
		//
		// state.getBoard().put("c3", Checker.WHITE);
		// state.getBoard().put("d3", Checker.WHITE);
		// state.getBoard().put("e3", Checker.WHITE);
		// state.getBoard().put("d2", Checker.WHITE);
		// state.getBoard().put("a1", Checker.WHITE);
		// state.getBoard().put("d1", Checker.WHITE);
		// state.getBoard().put("g1", Checker.WHITE);
		// MulinoClientFirstMiniMaxAlphaBeta.player = Checker.WHITE;
		// MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.BLACK;
		// a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 9);
		//
		// switch(state.getCurrentPhase()) {
		// case FIRST: state = Phase1.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// case SECOND: state = Phase2.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// case FINAL: state = PhaseFinal.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// default: throw new Exception("Illegal Phase");
		// }
		// System.out.println(state);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// PRIMO STATO DI SECONDA FASE DELLA PARTITA 7v7 CON ALPHA-BETA, PRIMA DELLA
		// CORREZIONE DEI SUCCESSORI
		// try {
		// state = new State();
		// state.setBlackCheckers(0);
		// state.setWhiteCheckers(0);
		// state.setCurrentPhase(State.Phase.SECOND);
		// state.setBlackCheckersOnBoard(7);
		// state.setWhiteCheckersOnBoard(8);
		// state.getBoard().put("a7", Checker.BLACK);
		// state.getBoard().put("g7", Checker.BLACK);
		// state.getBoard().put("b6", Checker.BLACK);
		// state.getBoard().put("d6", Checker.BLACK);
		// state.getBoard().put("f6", Checker.BLACK);
		// state.getBoard().put("e4", Checker.BLACK);
		// state.getBoard().put("d2", Checker.BLACK);
		//
		// state.getBoard().put("d7", Checker.WHITE);
		// state.getBoard().put("b4", Checker.WHITE);
		// state.getBoard().put("c3", Checker.WHITE);
		// state.getBoard().put("d3", Checker.WHITE);
		// state.getBoard().put("e3", Checker.WHITE);
		// state.getBoard().put("a1", Checker.WHITE);
		// state.getBoard().put("d1", Checker.WHITE);
		// state.getBoard().put("g1", Checker.WHITE);
		// MulinoClientFirstMiniMaxAlphaBeta.player = Checker.WHITE;
		// MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.BLACK;
		// a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 11);
		//
		// switch(state.getCurrentPhase()) {
		// case FIRST: state = Phase1.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// case SECOND: state = Phase2.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// case FINAL: state = PhaseFinal.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// default: throw new Exception("Illegal Phase");
		// }
		// System.out.println(state);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// STATO A CASO INTESO COME MASSIMA DIFFICOLTA' DI ESPANSIONE IN FASE 2, INFATTI
		// HA 9 BIANCHI E 9 NERI
		// try {
		// state = new State();
		// state.setBlackCheckers(0);
		// state.setWhiteCheckers(0);
		// state.setCurrentPhase(State.Phase.SECOND);
		// state.setBlackCheckersOnBoard(9);
		// state.setWhiteCheckersOnBoard(9);
		// state.getBoard().put("a7", Checker.BLACK);
		// state.getBoard().put("g7", Checker.BLACK);
		// state.getBoard().put("b6", Checker.BLACK);
		// state.getBoard().put("d6", Checker.BLACK);
		// state.getBoard().put("f6", Checker.BLACK);
		// state.getBoard().put("e4", Checker.BLACK);
		// state.getBoard().put("d2", Checker.BLACK);
		// state.getBoard().put("b2", Checker.BLACK);
		// state.getBoard().put("f4", Checker.BLACK);
		//
		// state.getBoard().put("d7", Checker.WHITE);
		// state.getBoard().put("b4", Checker.WHITE);
		// state.getBoard().put("c3", Checker.WHITE);
		// state.getBoard().put("d3", Checker.WHITE);
		// state.getBoard().put("e3", Checker.WHITE);
		// state.getBoard().put("a1", Checker.WHITE);
		// state.getBoard().put("d1", Checker.WHITE);
		// state.getBoard().put("g1", Checker.WHITE);
		// state.getBoard().put("c5", Checker.WHITE);
		// MulinoClientFirstMiniMaxAlphaBeta.player = Checker.WHITE;
		// MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.BLACK;
		// a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 16);
		//
		// switch(state.getCurrentPhase()) {
		// case FIRST: state = Phase1.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// case SECOND: state = Phase2.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// case FINAL: state = PhaseFinal.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// default: throw new Exception("Illegal Phase");
		// }
		// System.out.println(state);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// STATO IN CUI IL BIANCO DEVE VINCERE IN UNA MOSSA MA NON LO RICONOSCEVA
		// try {
		// state = new State();
		// state.setBlackCheckers(0);
		// state.setWhiteCheckers(0);
		// state.setCurrentPhase(State.Phase.FINAL);
		// state.setBlackCheckersOnBoard(3);
		// state.setWhiteCheckersOnBoard(3);
		// state.getBoard().put("d7", Checker.BLACK);
		// state.getBoard().put("g7", Checker.BLACK);
		// state.getBoard().put("g1", Checker.BLACK);
		//
		// state.getBoard().put("d2", Checker.WHITE);
		// state.getBoard().put("d3", Checker.WHITE);
		// state.getBoard().put("g4", Checker.WHITE);
		// System.out.println(state);
		//
		// MulinoClientFirstMiniMaxAlphaBeta.player = Checker.WHITE;
		// MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.BLACK;
		// a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 1);
		//
		// switch(state.getCurrentPhase()) {
		// case FIRST: state = Phase1.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// case SECOND: state = Phase2.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// case FINAL: state = PhaseFinal.applyMove(state, a,
		// MulinoClientFirstMiniMaxAlphaBeta.player); break;
		// default: throw new Exception("Illegal Phase");
		// }
		// System.out.println(state);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// STATO IN CUI IL BIANCO PERDE IN UNA MOSSA MA NON LO RICONOSCE
		// QUA NEL TEST INVECE TROVA LA MOSSA GIUSTA DA FARE, PERCHE' ALLORA
		// IN PARTITA SCEGLIEVA MOSSE CON VALORE 0 MA CHE PORTAVANO ALLA SCONFITTA
		// DIRETTA?
		// Ultimo stato di Test 1 - Server (White)
		// Current state is:
		// 7 W--------B--------O
		// 6 |--B-----O-----B--|
		// 5 |--|--W--O--O--|--|
		// 4 O--B--O O--O--B
		// 3 |--|--O--O--O--|--|
		// 2 |--B-----O-----O--|
		// 1 B--------O--------W
		// a b c d e f g
		// Phase: Final;
		// White Checkers: 0;
		// Black Checkers: 0;
		// White Checkers On Board: 3;
		// Black Checkers On Board: 7;
		// try {
		// state = new State();
		// state.setBlackCheckers(0);
		// state.setWhiteCheckers(0);
		// state.setCurrentPhase(State.Phase.FINAL);
		// state.setBlackCheckersOnBoard(7);
		// state.setWhiteCheckersOnBoard(3);
		// state.getBoard().put("d7", Checker.BLACK);
		// state.getBoard().put("b6", Checker.BLACK);
		// state.getBoard().put("f6", Checker.BLACK);
		// state.getBoard().put("b4", Checker.BLACK);
		// state.getBoard().put("g4", Checker.BLACK);
		// state.getBoard().put("b2", Checker.BLACK);
		// state.getBoard().put("a1", Checker.BLACK);
		//
		// state.getBoard().put("a7", Checker.WHITE);
		// state.getBoard().put("c5", Checker.WHITE);
		// state.getBoard().put("g1", Checker.WHITE);
		// System.out.println(state);
		//
		// MulinoClientFirstMiniMax.player = Checker.WHITE;
		// MulinoClientFirstMiniMax.otherPlayer = Checker.BLACK;
		// MulinoClientFirstMiniMax client = new
		// MulinoClientFirstMiniMax(Checker.WHITE);
		// a = client.iterativeDeepeningMinimaxDecision(state, 60000);
		//
		// switch(state.getCurrentPhase()) {
		// case FIRST: state = Phase1.applyMove(state, a,
		// MulinoClientFirstMiniMax.player); break;
		// case SECOND: state = Phase2.applyMove(state, a,
		// MulinoClientFirstMiniMax.player); break;
		// case FINAL: state = PhaseFinal.applyMove(state, a,
		// MulinoClientFirstMiniMax.player); break;
		// default: throw new Exception("Illegal Phase");
		// }
		// System.out.println(state);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// RIGA 1186 TEST 1 BLACK
		// Il riconoscimento dello stato di vittoria deve avvenire sempre a un livello
		// in meno
		// man mano che le mosse avanzano. Invece pare non succedere bene.
		// Current state is:
		// 7 W--------B--------O
		// 6 |--B-----B-----O--|
		// 5 |--|--O--W--O--|--|
		// 4 B--O--W O--O--B
		// 3 |--|--O--O--O--|--|
		// 2 |--B-----O-----O--|
		// 1 W--------B--------W
		// a b c d e f g
		// Phase: Second;
		// White Checkers: 0;
		// Black Checkers: 0;
		// White Checkers On Board: 5;
		// Black Checkers On Board: 7;

//		try {
//			state = new State();
//			state.setBlackCheckers(0);
//			state.setWhiteCheckers(0);
//			state.setCurrentPhase(State.Phase.SECOND);
//			state.setBlackCheckersOnBoard(7);
//			state.setWhiteCheckersOnBoard(7);
//			state.getBoard().put("a7", Checker.WHITE);
//			state.getBoard().put("d7", Checker.WHITE);
//			state.getBoard().put("g7", Checker.WHITE);
//			state.getBoard().put("b4", Checker.WHITE);
//			state.getBoard().put("c4", Checker.WHITE);
//			state.getBoard().put("d5", Checker.WHITE);
//			state.getBoard().put("d2", Checker.WHITE);
//
//			state.getBoard().put("a4", Checker.BLACK);
//			state.getBoard().put("e4", Checker.BLACK);
//			state.getBoard().put("g4", Checker.BLACK);
//			state.getBoard().put("b2", Checker.BLACK);
//			state.getBoard().put("f2", Checker.BLACK);
//			state.getBoard().put("a1", Checker.BLACK);
//			state.getBoard().put("d1", Checker.BLACK);
//			System.out.println(state);
//			
//			MulinoClientFirstMiniMaxAlphaBetaKiller client = new MulinoClientFirstMiniMaxAlphaBetaKiller(Checker.WHITE);
//			
//			MulinoClientFirstMiniMaxAlphaBetaKiller.player = Checker.WHITE;
//			MulinoClientFirstMiniMaxAlphaBetaKiller.otherPlayer = Checker.BLACK;
//			a = client.iterativeDeepeningMinimaxDecision(state, 60000);
//
//			switch (state.getCurrentPhase()) {
//			case FIRST:
//				state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
//				break;
//			case SECOND:
//				state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
//				break;
//			case FINAL:
//				state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
//				break;
//			default:
//				throw new Exception("Illegal Phase");
//			}
//			System.out.println(state);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		try {
//			state = new State();
//			state.setBlackCheckers(0);
//			state.setWhiteCheckers(0);
//			state.setCurrentPhase(State.Phase.SECOND);
//			state.setBlackCheckersOnBoard(4);
//			state.setWhiteCheckersOnBoard(7);
//			state.getBoard().put("b6", Checker.WHITE);
//			state.getBoard().put("d6", Checker.WHITE);
//			state.getBoard().put("g7", Checker.WHITE);
//			state.getBoard().put("d2", Checker.WHITE);
//
//			state.getBoard().put("a4", Checker.BLACK);
//			state.getBoard().put("e4", Checker.BLACK);
//			state.getBoard().put("f4", Checker.BLACK);
//			state.getBoard().put("b2", Checker.BLACK);
//			state.getBoard().put("a1", Checker.BLACK);
//			state.getBoard().put("d1", Checker.BLACK);
//			state.getBoard().put("g1", Checker.BLACK);
//			System.out.println(state);
//			
//			MulinoClientFirstMiniMaxAlphaBetaKiller client = new MulinoClientFirstMiniMaxAlphaBetaKiller(Checker.WHITE);
//			
//			MulinoClientFirstMiniMaxAlphaBetaKiller.player = Checker.WHITE;
//			MulinoClientFirstMiniMaxAlphaBetaKiller.otherPlayer = Checker.BLACK;
//			a = client.iterativeDeepeningMinimaxDecision(state, 60000);
//
//			switch (state.getCurrentPhase()) {
//			case FIRST:
//				state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
//				break;
//			case SECOND:
//				state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
//				break;
//			case FINAL:
//				state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
//				break;
//			default:
//				throw new Exception("Illegal Phase");
//			}
//			System.out.println(state);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		try {
//			state = new State();
//			state.setBlackCheckers(0);
//			state.setWhiteCheckers(0);
//			state.setCurrentPhase(State.Phase.SECOND);
//			state.setBlackCheckersOnBoard(8);
//			state.setWhiteCheckersOnBoard(4);
//			state.getBoard().put("f4", Checker.WHITE);
//			state.getBoard().put("d6", Checker.WHITE);
//			state.getBoard().put("b4", Checker.WHITE);
//			state.getBoard().put("d2", Checker.WHITE);
//
//			state.getBoard().put("d7", Checker.BLACK);
//			state.getBoard().put("b6", Checker.BLACK);
//			state.getBoard().put("c5", Checker.BLACK);
//			state.getBoard().put("d5", Checker.BLACK);
//			state.getBoard().put("c4", Checker.BLACK);
//			state.getBoard().put("e4", Checker.BLACK);
//			state.getBoard().put("c3", Checker.BLACK);
//			state.getBoard().put("a1", Checker.BLACK);
			state = new State();
			state.setBlackCheckers(0);
			state.setWhiteCheckers(0);
			state.setCurrentPhase(State.Phase.SECOND);
			state.setBlackCheckersOnBoard(8);
			state.setWhiteCheckersOnBoard(7);
			state.getBoard().put("a7", Checker.WHITE);
			state.getBoard().put("g7", Checker.WHITE);
			state.getBoard().put("d6", Checker.WHITE);
			state.getBoard().put("d5", Checker.WHITE);
			state.getBoard().put("b4", Checker.WHITE);
			state.getBoard().put("c4", Checker.WHITE);
			state.getBoard().put("d3", Checker.WHITE);

			state.getBoard().put("a4", Checker.BLACK);
			state.getBoard().put("e4", Checker.BLACK);
			state.getBoard().put("b2", Checker.BLACK);
			state.getBoard().put("d2", Checker.BLACK);
			state.getBoard().put("f2", Checker.BLACK);
			state.getBoard().put("a1", Checker.BLACK);
			state.getBoard().put("d1", Checker.BLACK);
			state.getBoard().put("g1", Checker.BLACK);
			System.out.println(state);
			
			MulinoClientFirstMiniMax client = new MulinoClientFirstMiniMax(Checker.BLACK);
			
			MulinoClientFirstMiniMax.player = Checker.BLACK;
			MulinoClientFirstMiniMax.otherPlayer = Checker.WHITE;
			a = client.iterativeDeepeningMinimaxDecision(state, 60000);

			switch (state.getCurrentPhase()) {
			case FIRST:
				state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player);
				break;
			case SECOND:
				state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player);
				break;
			case FINAL:
				state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player);
				break;
			default:
				throw new Exception("Illegal Phase");
			}
			System.out.println(state);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doTestIterativeDeepening() {
		MulinoClientFirstMiniMax.player = Checker.WHITE;
		MulinoClientFirstMiniMax.otherPlayer = Checker.BLACK;
		State initialState = new State();
		State state;
		MulinoClientFirstMiniMax client = null;
		try {
			client = new MulinoClientFirstMiniMax(Checker.WHITE);
		} catch (IOException e1) {
			// e1.printStackTrace();
		}

		try {
			Action a = client.iterativeDeepeningMinimaxDecision(initialState, 60000);
			switch (initialState.getCurrentPhase()) {
			case FIRST:
				state = Phase1.applyMove(initialState, a, MulinoClientFirstMiniMax.player);
				break;
			case SECOND:
				state = Phase2.applyMove(initialState, a, MulinoClientFirstMiniMax.player);
				break;
			case FINAL:
				state = PhaseFinal.applyMove(initialState, a, MulinoClientFirstMiniMax.player);
				break;
			default:
				throw new Exception("Illegal Phase");
			}
			System.out.println(state);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void doTestIterativeDeepeningAlphaBeta() {
		MulinoClientFirstMiniMaxAlphaBeta.player = Checker.WHITE;
		MulinoClientFirstMiniMaxAlphaBeta.otherPlayer = Checker.BLACK;
		State initialState = new State();
		State state;
		MulinoClientFirstMiniMaxAlphaBeta client = null;
		try {
			client = new MulinoClientFirstMiniMaxAlphaBeta(Checker.WHITE);
		} catch (IOException e1) {
			// e1.printStackTrace();
		}

		try {
			Action a = client.iterativeDeepeningMinimaxDecision(initialState, 60000);
			switch (initialState.getCurrentPhase()) {
			case FIRST:
				state = Phase1.applyMove(initialState, a, MulinoClientFirstMiniMaxAlphaBeta.player);
				break;
			case SECOND:
				state = Phase2.applyMove(initialState, a, MulinoClientFirstMiniMaxAlphaBeta.player);
				break;
			case FINAL:
				state = PhaseFinal.applyMove(initialState, a, MulinoClientFirstMiniMaxAlphaBeta.player);
				break;
			default:
				throw new Exception("Illegal Phase");
			}
			System.out.println(state);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doTestIterativeDeepeningKiller() {
		MulinoClientFirstMiniMaxAlphaBetaKiller.player = Checker.WHITE;
		MulinoClientFirstMiniMaxAlphaBetaKiller.otherPlayer = Checker.BLACK;
		State initialState = new State();
		State state;
		MulinoClientFirstMiniMaxAlphaBetaKiller client = null;
		try {
			client = new MulinoClientFirstMiniMaxAlphaBetaKiller(Checker.WHITE);
		} catch (IOException e1) {
			// e1.printStackTrace();
		}

		try {
			Action a = client.iterativeDeepeningMinimaxDecision(initialState, 60000*10);
			switch (initialState.getCurrentPhase()) {
			case FIRST:
				state = Phase1.applyMove(initialState, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
				break;
			case SECOND:
				state = Phase2.applyMove(initialState, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
				break;
			case FINAL:
				state = PhaseFinal.applyMove(initialState, a, MulinoClientFirstMiniMaxAlphaBetaKiller.player);
				break;
			default:
				throw new Exception("Illegal Phase");
			}
			System.out.println(state);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void doTestNegamax() {
		State state = new State();
		Action a;

		try {
			for (int i = 0; i < 1000; i++) {
				MulinoClientFirstNegamax.player = Checker.WHITE;
				MulinoClientFirstNegamax.otherPlayer = Checker.BLACK;
				a = MulinoClientFirstNegamax.negamaxDecision(state, 4);

				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstNegamax.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstNegamax.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstNegamax.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstNegamax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstNegamax.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstNegamax.statesAlreadySeen.add(state);

				MulinoClientFirstNegamax.player = Checker.BLACK;
				MulinoClientFirstNegamax.otherPlayer = Checker.WHITE;
				a = MulinoClientFirstNegamax.negamaxDecision(state, 4);
				switch (state.getCurrentPhase()) {
				case FIRST:
					state = Phase1.applyMove(state, a, MulinoClientFirstNegamax.player);
					break;
				case SECOND:
					state = Phase2.applyMove(state, a, MulinoClientFirstNegamax.player);
					break;
				case FINAL:
					state = PhaseFinal.applyMove(state, a, MulinoClientFirstNegamax.player);
					break;
				default:
					throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstNegamax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstNegamax.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i + 1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstNegamax.statesAlreadySeen.add(state);

				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
