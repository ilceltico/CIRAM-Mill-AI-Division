package it.unibo.ai.didattica.mulino.tests;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1;
import it.unibo.ai.didattica.mulino.actions.Phase2;
import it.unibo.ai.didattica.mulino.actions.PhaseFinal;
import it.unibo.ai.didattica.mulino.actions.WrongPhaseException;
import it.unibo.ai.didattica.mulino.client.MulinoClientFirstMiniMax;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;

public class MulinoClientFirstMiniMaxTest {
	
	public static void main(String[] args) {
//		doTest1();
		doTest2();
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
			for (int i=0; i<30; i++) {
				MulinoClientFirstMiniMax.player = Checker.WHITE;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 3);
				
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case SECOND: state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				
				MulinoClientFirstMiniMax.player = Checker.BLACK;
				a = MulinoClientFirstMiniMax.minimaxDecision(state, 1);
				
				switch(state.getCurrentPhase()) {
				case FIRST: state = Phase1.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case SECOND: state = Phase2.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				case FINAL: state = PhaseFinal.applyMove(state, a, MulinoClientFirstMiniMax.player); break;
				default: throw new Exception("Illegal Phase");
				}
				System.out.println(state);
				

				System.out.println(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
