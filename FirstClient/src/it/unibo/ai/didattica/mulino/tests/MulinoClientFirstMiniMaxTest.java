package it.unibo.ai.didattica.mulino.tests;

import java.io.BufferedReader;
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
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;
import it.unibo.ai.didattica.mulino.domain.State.Phase;

public class MulinoClientFirstMiniMaxTest {
	
	public static void main(String[] args) {
//		doTest1();
//		doTest2();
		doTest2WithAlphaBeta();
//		playAgaintsWhiteCPU();
//		playAgainstBlackCPU();
	}
	
	public static void doTest1() {
		MulinoClientFirstMiniMax.player = Checker.WHITE;
		State initialState = new State();
		
		try {
			MulinoClientFirstMiniMax.minimaxDecision(initialState, 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void doTest2() {
		State state = new State();
		Action a;

		try {
			for (int i=0; i<1000; i++) {
				MulinoClientFirstMiniMax.player = Checker.WHITE;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 5);
				
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case SECOND: state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i+1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i+1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);
				

				MulinoClientFirstMiniMax.player = Checker.BLACK;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 5);
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case SECOND: state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i+1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i+1) + " mosse");
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
			for (int i=0; i<1000; i++) {
				MulinoClientFirstMiniMaxAlphaBeta.player = Checker.WHITE;
				a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 5);
				
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player); break;
				case SECOND: state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i+1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i+1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.add(state);
				

				MulinoClientFirstMiniMaxAlphaBeta.player = Checker.BLACK;
				a = MulinoClientFirstMiniMaxAlphaBeta.minimaxDecision(state, 5);
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player); break;
				case SECOND: state = Phase2.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMaxAlphaBeta.player); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i+1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMaxAlphaBeta.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i+1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMaxAlphaBeta.statesAlreadySeen.add(state);
				

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
			for (int i=0; i<1000; i++) {
				MulinoClientFirstMiniMax.player = Checker.WHITE;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 1);
				
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case SECOND: state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i+1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i+1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);
				
				System.out.println("Player BLACK, do your move: ");
				String actionString = in.readLine();
				a = stringToAction(actionString, state.getCurrentPhase());
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, Checker.BLACK); break;
				case SECOND: state = Phase2.applyMove(state, a, Checker.BLACK); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, Checker.BLACK); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i+1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i+1) + " mosse");
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
			for (int i=0; i<1000; i++) {
				System.out.println("Player WHITE, do your move: ");
				String actionString = in.readLine();
				a = stringToAction(actionString, state.getCurrentPhase());
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, Checker.WHITE); break;
				case SECOND: state = Phase2.applyMove(state, a, Checker.WHITE); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, Checker.WHITE); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal W in " + (i+1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.WHITE)) {
					System.out.println("Vittoria W in " + (i+1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);
				
				MulinoClientFirstMiniMax.player = Checker.BLACK;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 1);
				
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case SECOND: state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				if (MulinoClientFirstMiniMax.statesAlreadySeen.contains(state)) {
					System.out.println("Pareggio scatenato dal B in " + (i+1) + " mosse");
					System.exit(0);
				}
				if (MulinoClientFirstMiniMax.isWinningState(state, Checker.BLACK)) {
					System.out.println("Vittoria B in " + (i+1) + " mosse");
					System.exit(0);
				}
				MulinoClientFirstMiniMax.statesAlreadySeen.add(state);
				

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
}
